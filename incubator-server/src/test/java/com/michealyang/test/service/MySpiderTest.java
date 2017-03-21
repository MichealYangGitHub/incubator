package com.michealyang.test.service;

import com.michealyang.domain.houseSpy.mySpider.MyRequest;
import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import com.michealyang.service.houseSpy.spider.mySpider.MySpider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by michealyang on 17/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class MySpiderTest {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void getText() {
        String url = "https://github.com/code4craft";
        MySpider mySpider = new MySpider();
        MyRequest request = new MyRequest();
        request.setUrl(url);

        try {
            MyResponse response = mySpider.getMethod(request);
            logger.info("response=#{}",response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
