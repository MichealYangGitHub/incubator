package com.michealyang.model.houseSpy.dto;

import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.domain.LJHouseTrace;

import java.util.List;

/**
 * Created by michealyang on 17/3/28.
 */
public class LJHouseInfoDto {

    private LJHouse ljHouse;

    //历史价格
    private List<LJHouseTrace> ljHouseTraces;

    //显示的时间范围。注：timeSpan应该和ljHouseTraces的大小相同
    private List<String> timeSpan;

    public LJHouse getLjHouse() {
        return ljHouse;
    }

    public void setLjHouse(LJHouse ljHouse) {
        this.ljHouse = ljHouse;
    }

    public List<LJHouseTrace> getLjHouseTraces() {
        return ljHouseTraces;
    }

    public void setLjHouseTraces(List<LJHouseTrace> ljHouseTraces) {
        this.ljHouseTraces = ljHouseTraces;
    }

    public List<String> getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(List<String> timeSpan) {
        this.timeSpan = timeSpan;
    }

    @Override
    public String toString() {
        return "LJHouseInfoDto{" +
                "ljHouse=" + ljHouse +
                ", ljHouseTraces=" + ljHouseTraces +
                ", timeSpan=" + timeSpan +
                '}';
    }
}
