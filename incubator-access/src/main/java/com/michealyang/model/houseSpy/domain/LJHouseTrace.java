package com.michealyang.model.houseSpy.domain;

/**
 * Created by michealyang on 17/3/16.
 */
public class LJHouseTrace {
    private Integer id;
    private Long houseId;
    private Float total;
    private Float unitPrice;    //单价
    private Integer ctime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getCtime() {
        return ctime;
    }

    public void setCtime(Integer ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        return "LJHouseTrace{" +
                "id=" + id +
                ", houseId=" + houseId +
                ", total=" + total +
                ", unitPrice=" + unitPrice +
                ", ctime=" + ctime +
                '}';
    }
}
