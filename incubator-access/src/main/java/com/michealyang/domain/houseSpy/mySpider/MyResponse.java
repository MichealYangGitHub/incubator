package com.michealyang.domain.houseSpy.mySpider;

import java.util.Map;

/**
 * Created by michealyang on 17/3/17.
 */
public class MyResponse {

    //请求的url
    private String url;

    //状态码
    private int statusCode;

    //响应头
    private Map<String, String> responseHeaders;

    //响应体
    private MyDocument responseBody;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public MyDocument getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(MyDocument responseBody) {
        this.responseBody = responseBody;
    }


    @Override
    public String toString() {
        return "MyResponse{" +
                "statusCode=" + statusCode +
                ", responseHeaders=" + responseHeaders +
                '}';
    }
}
