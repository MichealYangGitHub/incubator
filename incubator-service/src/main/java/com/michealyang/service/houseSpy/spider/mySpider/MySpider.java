package com.michealyang.service.houseSpy.spider.mySpider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.michealyang.domain.houseSpy.mySpider.MyDocument;
import com.michealyang.domain.houseSpy.mySpider.MyRequest;
import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * Created by michealyang on 17/3/16.
 */
@Service
public class MySpider {
    private static final Logger logger = LoggerFactory.getLogger(MySpider.class);


    public MyResponse getMethod(MyRequest request) throws IOException {
        logger.info("[MySpider.getText] request=#{}", request);
        Preconditions.checkArgument(request != null
                && StringUtils.isNotBlank(request.getUrl()),
                "请指定请求的URL");
        HttpClient client = getHttpClient();
        HttpGet httpGet = new HttpGet(request.getUrl());
        HttpResponse httpResponse = client.execute(httpGet);

        MyResponse response = new MyResponse();
        response.setUrl(request.getUrl());
        response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        response.setResponseHeaders(getResponseHeaders(httpResponse));
        response.setResponseBody(getResponseBody(httpResponse));

        return response;
    }

    /**
     * 获取response headers
     * @param httpResponse
     * @return
     */
    public Map<String, String> getResponseHeaders(HttpResponse httpResponse){
        Preconditions.checkArgument(httpResponse != null);
        Header[] headers = httpResponse.getAllHeaders();
        if(headers == null || headers.length <= 0) return Collections.emptyMap();
        Map<String, String> responseHeaders = Maps.newHashMap();
        for(Header header : headers) {
            if(header == null) continue;
            if(StringUtils.isBlank(header.getName())) continue;
            responseHeaders.put(header.getName(), header.getValue());
        }
        return responseHeaders;
    }

    /**
     * 获取响应内容，包括原始的html和经过解析后的Document
     * @param httpResponse
     * @return
     */
    public MyDocument getResponseBody(HttpResponse httpResponse){
        Preconditions.checkArgument(httpResponse != null);
        MyDocument myDocument = new MyDocument();
        String html = getRawHtml(httpResponse);
        myDocument.setHtml(html);
        if(StringUtils.isBlank(html)) return myDocument;

        Document document = Jsoup.parse(html);
        myDocument.setJsoupDoucument(document);

        if(document == null) return myDocument;
        myDocument.setTitle(document.title());

        return myDocument;
    }

    /**
     * 获得最原始的HTML内容
     * @param httpResponse
     * @return
     */
    public String getRawHtml(HttpResponse httpResponse){
        Preconditions.checkArgument(httpResponse != null);
        HttpEntity entity = httpResponse.getEntity();
        if(entity == null) return null;
        try {
            return EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            logger.error("[MySpider.getRawHtml] 获取HTML失败", e);
        }
        return null;
    }

    public CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        CloseableHttpClient client = httpClientBuilder.build();
        return client;
    }

}
