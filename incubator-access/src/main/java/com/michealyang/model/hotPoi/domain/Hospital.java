package com.michealyang.model.hotPoi.domain;

/**
 * Created by michealyang on 17/3/3.
 */
public class Hospital {

    private int id;

    /*名称，如首都医科大学朝阳医院*/
    private String name;

    /*别名，一般指人们常说的简称，如朝阳医院*/
    private String alias;

    /*级别*/
    private int level;

    /*地址*/
    private String location;

    /*经纬度*/
    private String lnglat;

    /*创建时间*/
    private int ctime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLnglat() {
        return lnglat;
    }

    public void setLnglat(String lnglat) {
        this.lnglat = lnglat;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", level=" + level +
                ", location='" + location + '\'' +
                ", lnglat='" + lnglat + '\'' +
                ", ctime=" + ctime +
                '}';
    }
}
