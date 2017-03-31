package com.michealyang.service.houseSpy.lianjia.processor;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.michealyang.model.houseSpy.domain.mySpider.MyDocument;
import com.michealyang.model.houseSpy.domain.mySpider.MyResponse;
import com.michealyang.model.houseSpy.dto.LJHouseInfo;
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
 * Created by michealyang on 17/3/19.
 */
public class LJMySpiderConvertorForWeb implements IConvertor<MyResponse, LJHouseInfo> {
    private static final Logger logger = LoggerFactory.getLogger(LJMySpiderConvertorForMobile.class);


    @Override
    public LJHouseInfo doAction(MyResponse data) {
        logger.info("[doAction]");
        Preconditions.checkArgument(data != null, "data不能为null");
        Preconditions.checkArgument(data.getResponseBody() != null, "ResponseBody不能为null");
        Preconditions.checkArgument(data.getResponseBody().getJsoupDoucument() != null, "JsoupDocument不能为null");

        LJHouseInfo ljHouseInfo = new LJHouseInfo();
        MyDocument myDocument = data.getResponseBody();
        ljHouseInfo.setUrl(data.getUrl());
        ljHouseInfo.setHouseId(getHouseId(data.getUrl()));
        Document doc = myDocument.getJsoupDoucument();

        ljHouseInfo.setImgs(getImgs(doc));

        Elements price = doc.select("div.price");
        if(CollectionUtils.isEmpty(price)){
            logger.error("[doAction] 链家格式有改动，请确认 - price 找不到");
            return null;
        }

        Elements houseInfo = doc.select("div.houseInfo");
        if(CollectionUtils.isEmpty(houseInfo)){
            logger.error("[doAction] 链家格式有改动，请确认 - houseInfo 找不到");
            return null;
        }
        //总价
        Elements total = price.select("span.total");
        if(total.size() == 0){
            logger.error("[doAction] 链家格式有改动，请确认 - total获取失败");
            return null;
        }

        //房型
        Elements houseType = houseInfo.select("div.room").select("div.mainInfo");
        ljHouseInfo.setHouseType(houseType.text());

        //面积
        Elements area = houseInfo.select("div.area").select("div.mainInfo");
        Elements builtYear = houseInfo.select("div.area").select("div.subInfo");
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(builtYear.text());
        m.find();
        String builtYearStr = m.group();
        if(StringUtils.isBlank(builtYearStr)){
            logger.error("[doAction] built Year 获取失败。忽略");
        }else {
            ljHouseInfo.setBuiltYear(Integer.valueOf(builtYearStr));
        }



        ljHouseInfo.setTitle(myDocument.getTitle() == null ? "" : myDocument.getTitle());
        ljHouseInfo.setArea(getArea(area.text()));
        ljHouseInfo.setTotal(Integer.valueOf(total.text()));
        //单价
        ljHouseInfo.setUnitPrice(ljHouseInfo.getTotal() / ljHouseInfo.getArea() * 10000);

        Elements communityName = doc.select("div.communityName").select(".info");
        ljHouseInfo.setCommunity(communityName.text());

        //是否下架
        Elements offShelfTag = doc.select("img.remove_tag");
        if(CollectionUtils.isNotEmpty(offShelfTag)){
            ljHouseInfo.setOffShelf(1);
        }

        return ljHouseInfo;
    }

    private long getHouseId(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Pattern p= Pattern.compile("\\d+");
        Matcher m=p.matcher(url);
        if(m.find()) {
            return Long.valueOf(m.group());
        }
        return 0l;
    }

    private float getArea(String area) {
        Pattern p = Pattern.compile("\\d+\\.?\\d+");
        Matcher m = p.matcher(area);
        if(m.find()) {
            return Float.valueOf(m.group());
        }
        return 0.0f;
    }

    private String getImgs(Document doc){
        Elements picUls = doc.select(".smallpic");
        if(CollectionUtils.isEmpty(picUls)) return "";
        Elements lis = picUls.get(0).select("li");
        if(CollectionUtils.isEmpty(lis)) return "";

        List<String> urls = Lists.newArrayList();
        for(Element li : lis){
            if(li == null) continue;
            String url = li.attr("data-src");
            if(StringUtils.isBlank(url)) continue;
            urls.add(url);
        }

        if(CollectionUtils.isNotEmpty(urls)) {
            return StringUtils.join(urls, ",");
        }
        return "";
    }


    public static void main(String[] args) throws IOException {
        File input = new File("/Users/michealyang/Downloads/lianjiaWeb.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
//        Elements elements = doc.select("p.red");
//        Elements total = elements.select("span[data-mark=price]");
//        Element houseType = elements.get(1);
//        Element area = elements.get(2);
//        System.out.println(total.text());
//
//        System.out.println(houseType.text());
//        System.out.println(area.text());
//
//        Elements houseDesc = doc.select("ul.house_description");
//        Elements elements1 = houseDesc.select("li.short");
//        String builtYearStr = elements1.get(8).text();
//        String builtYear = builtYearStr.substring(3, builtYearStr.length() - 1);
//        Elements elements2 = houseDesc.select("li.long");
//        Element communityEle = elements2.get(3);
//        String community = communityEle.select("a").text().substring(3);

//        MyResponse response = new MyResponse();
//        response.setUrl("https://m.lianjia.com/bj/ershoufang/101101194178.html");
//        MyDocument myDocument = new MyDocument();
//        myDocument.setJsoupDoucument(doc);
//        response.setResponseBody(myDocument);
//
//        LJHouseInfo ljHouse = new LJMySpiderConvertorForWeb().doAction(response);

        LJMySpiderConvertorForWeb web = new LJMySpiderConvertorForWeb();
        System.out.println(web.getImgs(doc));

    }
}
