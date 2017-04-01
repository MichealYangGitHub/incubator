package com.michealyang.test.service.houseSpy;

import com.google.common.collect.Maps;
import com.michealyang.dao.houseSpy.LJHouseDao;
import com.michealyang.model.houseSpy.domain.LJHouse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by michealyang on 17/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class LJHouseDaoTest {
    private final static Logger logger = LoggerFactory.getLogger(LJHouseDaoTest.class);

    @Resource
    private LJHouseDao ljHouseDao;

    @Test
    public void getHousesByConds() {
        Map<String, Object> conds = Maps.newHashMap();
        conds.put("community", "月季园");
        List<LJHouse> ljHouseList = ljHouseDao.getHousesByConds(conds);

        System.out.println(ljHouseList);

    }
}
