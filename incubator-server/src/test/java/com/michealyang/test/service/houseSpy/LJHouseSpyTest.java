package com.michealyang.test.service.houseSpy;

import com.michealyang.service.houseSpy.lianjia.LJHouseSpy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by michealyang on 17/3/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class LJHouseSpyTest {

    private final static Logger logger = LoggerFactory.getLogger(LJHouseSpyTest.class);

    @Resource
    private LJHouseSpy ljHouseSpy;

    @Test
    public void crawlOneHouse(){
//        System.out.println(ljHouseSpy.crawlOneHouse("https://m.lianjia.com/bj/ershoufang/101101194178.html"));
//        System.out.println(ljHouseSpy.crawlOneHouse("https://bj.lianjia.com/ershoufang/101100954046.html"));
        System.out.println(ljHouseSpy.crawlOneHouse("https://m.lianjia.com/bj/ershoufang/101101179399.html"));

    }
}
