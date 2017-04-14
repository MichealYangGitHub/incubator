package com.michealyang.service.houseSpy.spider.mySpider;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.michealyang.model.houseSpy.domain.mySpider.MyDocument;
import com.michealyang.model.houseSpy.domain.mySpider.MyRequest;
import com.michealyang.model.houseSpy.domain.mySpider.MyResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
public class MySpiderWithProxy {
    private static final Logger logger = LoggerFactory.getLogger(MySpiderWithProxy.class);

    // 代理服务器
    final static String proxyHost = "proxy.abuyun.com";
    final static Integer proxyPort = 9020;

    // 代理隧道验证信息
    final static String proxyUser = "H8Y01B26BBF21M0D";
    final static String proxyPass = "15F9EB7AA845BA57";

    // IP切换协议头
    final static String switchIpHeaderKey = "Proxy-Switch-Ip";
    final static String switchIpHeaderVal = "yes";

    private static PoolingHttpClientConnectionManager cm = null;
    private static HttpRequestRetryHandler httpRequestRetryHandler = null;
    private static HttpHost proxy = null;

    private static CredentialsProvider credsProvider = null;
    private static RequestConfig reqConfig = null;

    static {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();

        Registry registry = RegistryBuilder.create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();

        cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(20);
        cm.setDefaultMaxPerRoute(5);

        proxy = new HttpHost(proxyHost, proxyPort, "http");

        credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(proxyUser, proxyPass));

        reqConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .setExpectContinueEnabled(false)
                .setProxy(new HttpHost(proxyHost, proxyPort))
                .build();
    }

    public MyResponse getMethod(MyRequest request) throws IOException {
        logger.info("[getText] request=#{}", request);
        Preconditions.checkArgument(request != null
                && StringUtils.isNotBlank(request.getUrl()),
                "请指定请求的URL");

        HttpGet httpGet = new HttpGet(request.getUrl());

        setHeaders(httpGet);

        httpGet.setConfig(reqConfig);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultCredentialsProvider(credsProvider)
                .build();

        AuthCache authCache = new BasicAuthCache();
        authCache.put(proxy, new BasicScheme());

        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);

        HttpResponse httpResponse = httpClient.execute(httpGet, localContext);

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

    /**
     * 设置请求头
     *
     * @param httpReq
     */
    private static void setHeaders(HttpRequestBase httpReq) {
        httpReq.setHeader("Accept-Encoding", null);
        httpReq.setHeader(switchIpHeaderKey, switchIpHeaderVal);
    }

}
