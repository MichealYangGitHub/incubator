package com.michealyang.model.houseSpy.dto;

/**
 * Created by michealyang on 17/3/21.
 */
public class HouseSpyQuery {
    //用户id
    private Integer userId;
    //房源id
    private Long houseId;
    //查询开始时间范围
    private String startTime;
    //查询截止时间范围
    private String endTime;
    //查询的时间范围，从当前时间往前推range天
    private Integer range;

    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "HouseSpyQuery{" +
                "userId=" + userId +
                ", houseId=" + houseId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", range=" + range +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", offset=" + offset +
                '}';
    }
}
