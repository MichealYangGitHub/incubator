package com.michealyang.service.houseSpy.lianjia;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.michealyang.db.dao.houseSpy.LJHouseDao;
import com.michealyang.db.dao.houseSpy.LJHouseTraceDao;
import com.michealyang.model.base.dto.ResultDto;
import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.domain.LJHouseTrace;
import com.michealyang.model.houseSpy.domain.mySpider.MyResponse;
import com.michealyang.model.houseSpy.dto.AgentTypeEnum;
import com.michealyang.model.houseSpy.dto.LJHouseInfo;
import com.michealyang.model.houseSpy.dto.LJHousePage;
import com.michealyang.service.houseSpy.IConvertor;
import com.michealyang.service.houseSpy.MyHouseSpyHelper;
import com.michealyang.service.houseSpy.lianjia.processor.LJMySpiderConvertorForMobile;
import com.michealyang.service.houseSpy.lianjia.processor.LJMySpiderConvertorForWeb;
import com.michealyang.service.houseSpy.lianjia.processor.LJMySpiderPageConvertor;
import com.michealyang.service.houseSpy.spider.SpiderStrategy;
import com.michealyang.service.houseSpy.spider.mySpider.MySpiderStrategy;
import com.michealyang.service.houseSpy.spider.mySpider.MySpiderWithProxyStrategy;
import com.michealyang.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by michealyang on 17/3/17.
 */
@Service
public class LJHouseSpy {
    private static final Logger logger = LoggerFactory.getLogger(LJHouseSpy.class);

    @Resource
    private MyHouseSpyHelper myHouseSpyHelper;

    @Resource
    private LJHouseDao ljHouseDao;

    @Resource
    private LJHouseTraceDao ljHouseTraceDao;

    @Resource
    private LJHouseService ljHouseService;

    /**
     * 抓取单个house内容，包含web版和mobile版
     * @param url   链接房源地址url。会对有效性进行校验
     * @param withProxy   是否使用代理
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultDto crawlOneHouse(String url, boolean withProxy) {
        logger.info("[crawlOneHouse] url=#{}, withProxy=#{}", url, withProxy);
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        if(!url.startsWith("http")){
            url = "https://" + url;
        }
        AgentTypeEnum agentTypeEnum = myHouseSpyHelper.getAgentType(url);
        if(AgentTypeEnum.INVALID.equals(agentTypeEnum)) {
            return new ResultDto(false, "请传入正确格式的url，或者请确认是否是链家的链接");
        }
        SpiderStrategy strategy = withProxy ? new MySpiderWithProxyStrategy() : new MySpiderStrategy();
        ResultDto resultDto;
        if(AgentTypeEnum.MOBILE.equals(agentTypeEnum)) {
            resultDto = doCrawl(url, strategy, new LJMySpiderConvertorForMobile());
        }else {
            resultDto = doCrawl(url, strategy, new LJMySpiderConvertorForWeb());
        }

        if(!resultDto.isSuccess()) {
            return resultDto;
        }
        return new ResultDto(true, Constants.SUCCESS, url);
    }

    /**
     * 批量爬取数据
     * @param urls
     * @param interval  爬取间隔，单位ms
     * @return
     */
    public ResultDto crwalHouses(List<String> urls, int interval, boolean withPrxoy){
        logger.info("[crwalHouses] urls=#{}, interval=#{}, withProxy=#{}", urls, interval, withPrxoy);
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(urls));

        int count = 0;
        for(String url : urls){
            if(StringUtils.isBlank(url)) continue;
            ResultDto res = crawlOneHouse(url, withPrxoy);
            logger.info("[crwalHouses] 爬取结果=#{}", res);
            if(!res.isSuccess()){
                logger.error("[crwalHouses] URL爬取失败。failedUrl=#{}", url);
                continue;
            }
            count++;
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                logger.error("[crwalHouses] Sleep Exception=#{}", e);
            }
        }
        if((float)count / urls.size() <0.5) {
            return new ResultDto(false, "爬取成功率不足50%");
        }
        return new ResultDto(true, "成功爬取了" + count + "条数据(总数:" + urls.size() + ")");
    }

    /**
     * 使用指定的策略来抓取网页内容
     * <p>使用指定的spiderStrategy来爬取内容，然后使用convertor将爬取内容转换成统一格式，最后将数据写入到数据库</p>
     * @param url
     * @param spiderStrategy    爬虫引擎选择
     * @param convertor 爬虫返回内容不一样，需要转换成统一的数据结构
     * @return  一切顺利，且数据写入到数据库，返回成功对象
     */
    public ResultDto doCrawl(String url, SpiderStrategy spiderStrategy, IConvertor convertor){
        logger.info("[LJHouseSpy.doCrawl] url=#{}, spiderStrategy=#{}, convertor=#{}",
                url, spiderStrategy, convertor);
        if(StringUtils.isBlank(url) || spiderStrategy == null || convertor == null) {
            return new ResultDto(false, Constants.ARGUMENT_FAILURE);
        }

        ResultDto<MyResponse> resultDto = spiderStrategy.crawlWeb(url);
        if(resultDto == null || !resultDto.isSuccess()) {
            return resultDto == null ? new ResultDto(false, "结果为空") : resultDto;
        }
        LJHouseInfo ljHouseInfo = (LJHouseInfo)convertor.doAction(resultDto.getData());
        if(ljHouseInfo == null) {
            return new ResultDto(false, "网页解析失败");
        }

        if(ljHouseDao.lock(ljHouseInfo.getHouseId()) == 0){ //不存在该房源的记录
            logger.info("[doCrawl] 第一次爬取房源。houseId=#{}", ljHouseInfo.getHouseId());
            LJHouse ljHouse = myHouseSpyHelper.convertLJHouseInfo2LJHouse(ljHouseInfo);
            LJHouseTrace ljHouseTrace = myHouseSpyHelper.convertLJHouseInfo2LJHouseTrace(ljHouseInfo);

            if(ljHouse.getOffShelf() != 0) {    //如果房源已经下架或已成交，则不添加Trace
                if(ljHouseInfo.getOffShelf() != 0) {
                    return new ResultDto(false, "该房源已下架或者已成交");
                }
//                logger.info("[doCrawl] 该房源已下架. houseId=#{}", ljHouseInfo.getHouseId());
//                ljHouse.setFinalTotal(ljHouseTrace.getTotal());
            }
            if(ljHouseDao.insert(ljHouse) <= 0){
                logger.error("[doCrawl] LJHouse数据库插入失败。ljHouse=#{}", ljHouse);
                return new ResultDto(false, Constants.SYS_FAILURE);
            }
            if(ljHouseTraceDao.insert(ljHouseTrace) <= 0){
                logger.error("[doCrawl] LJHouseTrace数据库插入失败。ljHouseTrace=#{}", ljHouseTrace);
                return new ResultDto(false, Constants.SYS_FAILURE);
            }
        }else{
            logger.info("[doCrawl] 已存在房源。houseId=#{}", ljHouseInfo.getHouseId());
            LJHouseTrace ljHouseTrace = myHouseSpyHelper.convertLJHouseInfo2LJHouseTrace(ljHouseInfo);
            if(ljHouseInfo.getOffShelf() != 0) { //如果房源已经下架，则更新LJHouse状态为下架状态
                logger.info("[doCrawl] 该房源已下架. houseId=#{}", ljHouseInfo.getHouseId());
                if (ljHouseDao.offShelfHouse(ljHouseInfo.getHouseId(), ljHouseTrace.getTotal()) <= 0) {
                    logger.error("[doCrawl] 更新上下架状态失败！ljHouseInfo=#{}", ljHouseInfo);
                    return new ResultDto(false, Constants.SYS_FAILURE);
                }
                return new ResultDto(true, Constants.SUCCESS);
            }
            if(ljHouseTraceDao.insert(ljHouseTrace) <= 0){
                logger.error("[doCrawl] LJHouseTrace数据库插入失败。ljHouseTrace=#{}", ljHouseTrace);
                return new ResultDto(false, Constants.SYS_FAILURE);
            }
        }

        return new ResultDto(true, Constants.SUCCESS);
    }

    public ResultDto crawlByPage(String pageUrl, boolean withPrxoy) {
        logger.info("[crawlByPage] pageUrls=#{}, withProxy=#{}", pageUrl, withPrxoy);
        ResultDto resultDto = crawlOnePage(pageUrl, withPrxoy);
        if(!resultDto.isSuccess()) {
            logger.error("[crawlByPage] 爬取page失败。PageUrl=#{}", pageUrl);
            return resultDto;
        }
        List<String> successUrls = Lists.newArrayList();
        successUrls.add(pageUrl);

        String nextPage = (String)resultDto.getData();
        while (StringUtils.isNotBlank(nextPage)) {
            ResultDto res = crawlOnePage(nextPage, withPrxoy);
            if(!res.isSuccess()) {
                logger.error("[crawlByPage] 爬取page失败。PageUrl=#{}", nextPage);
                return res;
            }
            successUrls.add(nextPage);
            nextPage = (String)res.getData();
        }

        logger.info("[crawlByPage] 成功爬取的page有successUrls=#{}", successUrls);

        return new ResultDto(true, Constants.SUCCESS, pageUrl);
    }

    private ResultDto crawlOnePage(String pageUrl, boolean withProxy){
        logger.info("[crawlOnePage] pageUrl=#{}", pageUrl);
        if(StringUtils.isBlank(pageUrl)) {
            return new ResultDto(false, Constants.ARGUMENT_FAILURE);
        }
        SpiderStrategy spiderStrategy = withProxy ? new MySpiderWithProxyStrategy() : new MySpiderStrategy();
        ResultDto resultDto = spiderStrategy.crawlWeb(pageUrl);
        if(!resultDto.isSuccess()) {
            return resultDto;
        }
        MyResponse response = (MyResponse)resultDto.getData();
        if(response == null) {
            return new ResultDto(false, Constants.SYS_FAILURE);
        }

        LJMySpiderPageConvertor convertor = new LJMySpiderPageConvertor();
        LJHousePage housePage = convertor.doAction(response);
        if(housePage == null) {
            return new ResultDto(false, "流量异常", pageUrl);
        }

        if(CollectionUtils.isNotEmpty(housePage.getTargetUrls())) {
            ResultDto res = ljHouseService.addSpies(housePage.getTargetUrls(), withProxy);
            logger.info("[crawlOnePage] res=#{}", res);
            if (!res.isSuccess()) {
                logger.error("[crawlOnePage] addSpies错误=#{}", res);
            }
        }

        logger.info("[crawlOnePage]成功爬取了页面pageUrl=#{}, 开始爬取下一页=#{}", pageUrl, housePage.getNextPage());
        return new ResultDto(true, Constants.SUCCESS, housePage.getNextPage());
    }
}
