package com.michealyang.util.test;

import com.michealyang.util.DateUtil;
import org.junit.Test;

import java.util.List;

/**
 * Created by michealyang on 17/3/30.
 */
public class DateUtilTest {

    @Test
    public void minusDays() {
        String res = DateUtil.minusDays(DateUtil.today(), 30);
        System.out.println(res);
    }

    @Test
    public void minusDaysArray() {
        List<String> res = DateUtil.minusDaysArray(DateUtil.today(), 29);
        System.out.println(res);
    }
}
