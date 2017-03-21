package com.michealyang.service.houseSpy.spider;

import com.michealyang.dto.ResultDto;

/**
 * Created by michealyang on 17/3/19.
 */
public interface SpiderStrategy<T> {
    /**
     * 爬取一个网页
     * @param url
     * @return
     */
    public ResultDto<T> crawlWeb(String url);

    /**
     * 爬取一个网站
     * @param url   网站首页
     * @param siteUrlStrategy   首页中要继续爬取url的获取策略
     * @return
     */
    public ResultDto<T> crawlSite(String url, ISiteUrlStrategy siteUrlStrategy);
}
