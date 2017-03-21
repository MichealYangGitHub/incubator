package com.michealyang.service.houseSpy.lianjia;

import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import com.michealyang.dto.ResultDto;

/**
 * Created by michealyang on 17/3/17.
 */
public class LJMySpiderStrategy implements ILJStrategy<MyResponse>{
    @Override
    public ResultDto processMobile(MyResponse data) {
        return null;
    }

    @Override
    public ResultDto processWeb(MyResponse data) {
        return null;
    }
}
