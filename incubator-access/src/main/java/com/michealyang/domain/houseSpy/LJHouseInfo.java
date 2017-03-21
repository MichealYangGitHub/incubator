package com.michealyang.domain.houseSpy;

/**
 * Created by michealyang on 17/3/16.
 */
public class LJHouseInfo {
    private Integer id;
    private String houseId;
    private String title;
    private int total;  //总价
    private float area; //面积
    private float unitPrice;    //单价
    private String houseType;   //房型
    private String community;   //小区
    private int offShelf;   //是否已经下架
    private int builtYear;  //建造年代
    private int ctime;  //创建时间



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getBuiltYear() {
        return builtYear;
    }

    public void setBuiltYear(int builtYear) {
        this.builtYear = builtYear;
    }

    public int getOffShelf() {
        return offShelf;
    }

    public void setOffShelf(int offShelf) {
        this.offShelf = offShelf;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        return "LJHouseInfo{" +
                "id=" + id +
                ", houseId='" + houseId + '\'' +
                ", title='" + title + '\'' +
                ", total=" + total +
                ", area=" + area +
                ", unitPrice=" + unitPrice +
                ", houseType='" + houseType + '\'' +
                ", community='" + community + '\'' +
                ", offShelf=" + offShelf +
                ", builtYear=" + builtYear +
                ", ctime=" + ctime +
                '}';
    }
}
