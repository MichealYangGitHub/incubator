package com.michealyang.model.houseSpy.dto;

/**
 * Created by michealyang on 17/3/16.
 */
public class LJHouseInfo {
    private Integer id;
    private long houseId;
    private String title;
    private float area; //面积
    private String houseType;   //房型
    private int total;  //总价
    private float unitPrice;    //单价
    private String community;   //小区
    private int offShelf;   //是否已经下架
    private String imgs;    //房源图片
    private String url; //房源链接
    private int builtYear;  //建造年代
    private int ctime;  //创建时间



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getHouseId() {
        return houseId;
    }

    public void setHouseId(long houseId) {
        this.houseId = houseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
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

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
                ", houseId=" + houseId +
                ", title='" + title + '\'' +
                ", area=" + area +
                ", houseType='" + houseType + '\'' +
                ", total=" + total +
                ", unitPrice=" + unitPrice +
                ", community='" + community + '\'' +
                ", offShelf=" + offShelf +
                ", imgs='" + imgs + '\'' +
                ", url='" + url + '\'' +
                ", builtYear=" + builtYear +
                ", ctime=" + ctime +
                '}';
    }
}
