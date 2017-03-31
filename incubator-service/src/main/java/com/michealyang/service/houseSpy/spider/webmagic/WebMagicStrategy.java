package com.michealyang.service.houseSpy.spider.webmagic;

import com.michealyang.model.base.dto.ResultDto;
import com.michealyang.service.houseSpy.spider.ISiteUrlStrategy;
import com.michealyang.service.houseSpy.spider.SpiderStrategy;

/**
 * Created by michealyang on 17/3/19.
 */
public class WebMagicStrategy implements SpiderStrategy{
    @Override
    public ResultDto<String> crawlWeb(String url) {
        return null;
    }

    @Override
    public ResultDto<String> crawlSite(String url, ISiteUrlStrategy siteUrlStrategy) {
        return null;
    }
}
