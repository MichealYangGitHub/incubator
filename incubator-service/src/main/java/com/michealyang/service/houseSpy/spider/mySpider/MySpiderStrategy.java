package com.michealyang.service.houseSpy.spider.mySpider;

import com.michealyang.domain.houseSpy.mySpider.MyRequest;
import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import com.michealyang.dto.ResultDto;
import com.michealyang.service.houseSpy.spider.ISiteUrlStrategy;
import com.michealyang.service.houseSpy.spider.SpiderStrategy;
import com.michealyang.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by michealyang on 17/3/16.
 */
@Service
public class MySpiderStrategy implements SpiderStrategy<MyResponse> {

    private static final Logger logger = LoggerFactory.getLogger(MySpiderStrategy.class);


    @Override
    public ResultDto<MyResponse> crawlWeb(String url) {
        logger.error("[MySpiderStrategy.crawlWeb] url=#{}", url);
        MyRequest request = new MyRequest();
        request.setUrl(url);

        MySpider mySpider = new MySpider();

        try {
            MyResponse response = mySpider.getMethod(request);
            return new ResultDto<MyResponse>(true, Constants.SUCCESS, response);
        } catch (IOException e) {
            logger.error("[MySpiderStrategy.crawlWeb] Exception: ", e);
        }
        return new ResultDto<MyResponse>(false, Constants.FAILURE);
    }

    @Override
    public ResultDto<MyResponse> crawlSite(String url, ISiteUrlStrategy siteUrlStrategy) {
        return null;
    }

}
