package com.michealyang.model.houseSpy.dto;

import java.util.List;

/**
 * Created by michealyang on 17/4/2.
 */
public class LJHousePage {
    //地铁线
    private int subway;

    //大区域，如西城、东城
    private String mainArea;

    //子区域，如五道口
    private String subArea;

    //
    private String url;

    //要收集的链接
    private List<String> targetUrls;

    //下一页
    private String nextPage;

    //总页数
    private int totalPages;

    public int getSubway() {
        return subway;
    }

    public void setSubway(int subway) {
        this.subway = subway;
    }

    public String getMainArea() {
        return mainArea;
    }

    public void setMainArea(String mainArea) {
        this.mainArea = mainArea;
    }

    public String getSubArea() {
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTargetUrls() {
        return targetUrls;
    }

    public void setTargetUrls(List<String> targetUrls) {
        this.targetUrls = targetUrls;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "LJHousePage{" +
                "subway=" + subway +
                ", mainArea='" + mainArea + '\'' +
                ", subArea='" + subArea + '\'' +
                ", url='" + url + '\'' +
                ", targetUrls=" + targetUrls +
                ", nextPage='" + nextPage + '\'' +
                ", totalPages=" + totalPages +
                '}';
    }
}
