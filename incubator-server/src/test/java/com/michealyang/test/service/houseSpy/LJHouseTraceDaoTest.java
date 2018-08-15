package com.michealyang.test.service.houseSpy;

import com.michealyang.db.dao.houseSpy.LJHouseTraceDao;
import com.michealyang.model.houseSpy.domain.LJHouseTrace;
import com.michealyang.util.DateUtil;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by michealyang on 17/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class LJHouseTraceDaoTest {
    private final static Logger logger = LoggerFactory.getLogger(LJHouseTraceDaoTest.class);

    @Resource
    private LJHouseTraceDao ljHouseTraceDao;

    @Test
    public void insert() {
        List<String> days = DateUtil.minusDaysArray(DateUtil.today(), 29);
        for(String day : days) {
            LJHouseTrace ljHouseTrace = new LJHouseTrace();
            ljHouseTrace.setHouseId(101101179399l);
            ljHouseTrace.setTotal((float)300 + RandomUtils.nextInt(20));
            ljHouseTrace.setUnitPrice(0.0f);
            ljHouseTrace.setCtime((int)DateUtil.Second2UT(day + " 12:00:00"));
            ljHouseTraceDao.insert(ljHouseTrace);
        }
    }
}
