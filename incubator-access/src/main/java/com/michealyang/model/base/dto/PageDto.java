package com.michealyang.model.base.dto;

/**
 * Created by michealyang on 17/4/2.
 */
public class PageDto<T> {
    private T data;
    private int totalSize;
    private int pageNums;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getPageNums() {
        return pageNums;
    }

    public void setPageNums(int pageNums) {
        this.pageNums = pageNums;
    }

    @Override
    public String toString() {
        return "PageDto{" +
                "data=" + data +
                ", totalSize=" + totalSize +
                ", pageNums=" + pageNums +
                '}';
    }
}
