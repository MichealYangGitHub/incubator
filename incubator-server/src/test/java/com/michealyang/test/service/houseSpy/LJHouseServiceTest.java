package com.michealyang.test.service.houseSpy;

import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import com.michealyang.model.houseSpy.dto.LJHouseInfoDto;
import com.michealyang.service.houseSpy.lianjia.LJHouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by michealyang on 17/3/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml"})
public class LJHouseServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(LJHouseServiceTest.class);

    @Resource
    private LJHouseService ljHouseService;

    @Test
    public void getHouseInfos() {
        HouseSpyQuery query = new HouseSpyQuery();
        List<LJHouseInfoDto> ljHouses = ljHouseService.getHouseInfos(query);
        System.out.println(ljHouses);
    }
}
