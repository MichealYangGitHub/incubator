package com.michealyang.domain.houseSpy.mySpider;

import java.util.Map;

/**
 * Created by michealyang on 17/3/17.
 */
public class MyRequest {
    //请求url
    private String url;

    //请求头
    private Map<String, Object> requestHeaders;

    //超时时间，单位s
    private int timeout;

    //重试次数
    private int retryTimes;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Object> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, Object> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public String toString() {
        return "MyRequest{" +
                "url='" + url + '\'' +
                ", requestHeaders=" + requestHeaders +
                ", timeout=" + timeout +
                ", retryTimes=" + retryTimes +
                '}';
    }
}
