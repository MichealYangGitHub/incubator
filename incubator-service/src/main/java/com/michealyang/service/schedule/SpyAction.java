package com.michealyang.service.schedule;

import com.michealyang.model.base.dto.ResultDto;
import com.michealyang.service.houseSpy.lianjia.LJHouseService;
import com.michealyang.service.houseSpy.lianjia.LJHouseSpy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**所有的Spider要开始行动了，go go go
 * Created by michealyang on 17/4/1.
 */
@Component
public class SpyAction {
    private static final Logger logger = LoggerFactory.getLogger(SpyAction.class);

    @Resource
    private LJHouseService ljHouseService;

    @Resource
    private LJHouseSpy ljHouseSpy;

    //每天中午12点开始爬取数据
    @Scheduled(cron = "0 0 12 * * ?")
    public void go() throws InterruptedException {
        logger.info("[go] Spider们出发了。。。");
        List<String> urls = ljHouseService.getAllOnShelfUrls();
        logger.info("[go] urls.size=#{}", urls.size());
        if(CollectionUtils.isEmpty(urls)){
            logger.info("[go] 我有兵甲三千，奈何无处可战啊！");
            return;
        }

        logger.info("[go] 开始爬取数据");
        long start = System.currentTimeMillis();
        for(String url : urls) {
            if(StringUtils.isBlank(url)) continue;
            ResultDto resultDto = ljHouseSpy.crawlOneHouse(url, true);
            logger.info("[go] resultDto=#{}", resultDto);
            if(!resultDto.isSuccess()){
                logger.error("[go] 报告，有错误！resultDto=#{}", resultDto);
            }
            //每爬取一个就歇5s，防止频率太快被发现
            Thread.sleep(5000);
        }

        logger.info("[go] 已完成全部抓取，总耗时为cost=#{} ms", System.currentTimeMillis() - start);
    }
}
