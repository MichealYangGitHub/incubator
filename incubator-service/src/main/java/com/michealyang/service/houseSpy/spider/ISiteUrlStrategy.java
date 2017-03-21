package com.michealyang.service.houseSpy.spider;

import java.util.List;

/**爬取一个site首页后，指定要继续爬取的子url策略
 * Created by michealyang on 17/3/19.
 */
public interface ISiteUrlStrategy<T> {
    List<String> getTargetUrls(T entity);
}
