package com.michealyang.service.houseSpy.lianjia;

import com.google.common.base.Preconditions;
import com.michealyang.dao.houseSpy.LJHouseDao;
import com.michealyang.domain.houseSpy.LJHouseInfo;
import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import com.michealyang.dto.AgentTypeEnum;
import com.michealyang.dto.ResultDto;
import com.michealyang.service.houseSpy.IConvertor;
import com.michealyang.service.houseSpy.MyHouseSpyHelper;
import com.michealyang.service.houseSpy.lianjia.processor.LJMySpiderConvertorForMobile;
import com.michealyang.service.houseSpy.lianjia.processor.LJMySpiderConvertorForWeb;
import com.michealyang.service.houseSpy.spider.SpiderStrategy;
import com.michealyang.service.houseSpy.spider.mySpider.MySpiderStrategy;
import com.michealyang.util.Constants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    /**
     * 抓取单个house内容，包含web版和mobile版
     * @param url   链接房源地址url。会对有效性进行校验
     * @return
     */
    public ResultDto crawlOneHouse(String url) {
        logger.info("[LJHouseSpy.crawlOneHouse] url=#{}", url);
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        if(!url.startsWith("http")){
            url = "https://" + url;
        }
        AgentTypeEnum agentTypeEnum = myHouseSpyHelper.getAgentType(url);
        if(AgentTypeEnum.INVALID.equals(agentTypeEnum)) {
            return new ResultDto(false, "请传入正确格式的url，或者请确认是否是链家的链接");
        }
        ResultDto resultDto;
        if(AgentTypeEnum.MOBILE.equals(agentTypeEnum)) {
            resultDto = doCrawl(url, new MySpiderStrategy(), new LJMySpiderConvertorForMobile());
        }else {
            resultDto = doCrawl(url, new MySpiderStrategy(), new LJMySpiderConvertorForWeb());
        }

        if(!resultDto.isSuccess()) {
            return resultDto;
        }
        return new ResultDto(true, Constants.SUCCESS, url);
    }

    public ResultDto crawlWholeSite(String url, ILJStrategy strategy, boolean isMobile) {
        return null;
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

        if(ljHouseDao.insert(ljHouseInfo) <= 0) {
            return new ResultDto(false, Constants.SYS_FAILURE);
        }

        return new ResultDto(true, Constants.SUCCESS);
    }
}
