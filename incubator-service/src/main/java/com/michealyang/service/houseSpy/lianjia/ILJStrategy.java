package com.michealyang.service.houseSpy.lianjia;

import com.michealyang.dto.ResultDto;

/**
 * Created by michealyang on 17/3/17.
 */
public interface ILJStrategy<T> {

    public ResultDto processMobile(T data);

    public ResultDto processWeb(T data);
}
