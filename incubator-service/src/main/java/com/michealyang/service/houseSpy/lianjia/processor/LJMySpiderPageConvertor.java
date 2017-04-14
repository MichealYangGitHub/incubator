package com.michealyang.service.houseSpy.lianjia.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.michealyang.model.houseSpy.domain.mySpider.MyDocument;
import com.michealyang.model.houseSpy.domain.mySpider.MyResponse;
import com.michealyang.model.houseSpy.dto.LJHousePage;
import com.michealyang.service.houseSpy.IConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michealyang on 17/4/2.
 */
public class LJMySpiderPageConvertor implements IConvertor<MyResponse, LJHousePage>{
    private static final Logger logger = LoggerFactory.getLogger(LJMySpiderPageConvertor.class);

    @Override
    public LJHousePage doAction(MyResponse data) {
        logger.info("[doAction]");
        Preconditions.checkArgument(data != null && data.getResponseBody() != null
        && data.getResponseBody().getJsoupDoucument() != null);
        MyDocument myDocument = data.getResponseBody();
        Document document = myDocument.getJsoupDoucument();

        LJHousePage ljHousePage = new LJHousePage();
        ljHousePage.setUrl(data.getUrl());
        Elements urlList = document.select("ul.sellListContent");
        if(CollectionUtils.isEmpty(urlList)) {
            logger.error("[doAction] 链家格式有改动，请确认 -- urlList找不到");
            logger.info("[HTML]: #{}", data.getResponseBody().getHtml());
            return null;
        }

        Elements lis = urlList.get(0).select("li");
        if(CollectionUtils.isEmpty(lis)) {
            logger.error("[doAction] 链家格式有改动，请确认 -- lis找不到");
            return null;
        }
        List<String> urls = Lists.newArrayList();
        for (Element li : lis) {
            if(li == null) continue;
            Elements img = li.select(".img");
            if(CollectionUtils.isEmpty(img)) {
                continue;
            }
            String url = img.attr("href");
            urls.add(url);

        }

        ljHousePage.setMainArea(getMainArea(data.getUrl()));
        ljHousePage.setSubArea("");
        ljHousePage.setTargetUrls(urls);

        Elements pages = document.select("div.house-lst-page-box");
        if(CollectionUtils.isEmpty(pages)) {
            return null;
        }
        String targetUrlTemplage = pages.get(0).attr("page-url");
        String pageData = pages.get(0).attr("page-data");
        JSONObject jsonObject = JSON.parseObject(pageData);
        int totalPages = (Integer) jsonObject.get("totalPage");
        int currPage = (Integer) jsonObject.get("curPage");
        if(currPage < totalPages){
            ljHousePage.setNextPage("http://" + getHost(data.getUrl()) + targetUrlTemplage.replace("{page}", Integer.toString(currPage+1)));
        }else {
            ljHousePage.setNextPage(null);
        }

        ljHousePage.setTotalPages(totalPages);

        return ljHousePage;
    }

    private String getMainArea(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Pattern p= Pattern.compile("\\w{2,8}\\.lianjia\\.com/ershoufang/(\\w+)");
        Matcher m=p.matcher(url);
        if(m.find()) {
            return m.group(1);
        }
        return null;
    }

    private String getHost(String url){
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Pattern p= Pattern.compile("(\\w{2,8}\\.lianjia\\.com)");
        Matcher m=p.matcher(url);
        if(m.find()) {
            return m.group(1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        File input = new File("/Users/michealyang/Downloads/lianjiaPage.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");

        LJMySpiderPageConvertor convertor = new LJMySpiderPageConvertor();

        String url = "http://bj.lianjia.com/ershoufang/dongcheng/";
        System.out.println(convertor.getMainArea(url));
        System.out.println(convertor.getHost(url));

        MyResponse response = new MyResponse();
        response.setUrl(url);
        MyDocument document = new MyDocument();
        document.setJsoupDoucument(doc);
        response.setResponseBody(document);
        LJHousePage page = convertor.doAction(response);
        System.out.println(page);

    }
}
