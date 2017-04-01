package com.michealyang.service.houseSpy.lianjia.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class LJMySpiderConvertorForMobile implements IConvertor<MyResponse, LJHouseInfo> {
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
        ljHouseInfo.setTitle(myDocument.getTitle() == null ? "" : myDocument.getTitle());

        Document doc = myDocument.getJsoupDoucument();
        ljHouseInfo.setImgs(getImgs(doc));

        //是否下架
        Elements dealedTag = doc.select("div.signed_logo");     //成交标识
        if(CollectionUtils.isNotEmpty(dealedTag)) {
            return parseDealed(ljHouseInfo, doc);
        }else {
            return parseNormal(ljHouseInfo, doc);
        }
    }

    /**
     * 解析普通页面和下架页面
     * @param ljHouseInfo
     * @param doc
     * @return
     */
    private LJHouseInfo parseNormal(LJHouseInfo ljHouseInfo, Document doc){
        logger.info("[parseNormal]");
        Elements redBig = doc.select("p.red");
        if(CollectionUtils.isEmpty(redBig) || redBig.size() != 3){
            logger.error("[doAction] 链家格式有改动，请确认 - red big 找不到");
            return null;
        }
        //总价
        Elements total = redBig.select("span[data-mark=price]");
        if(total.size() == 0){
            logger.error("[doAction] 链家格式有改动，请确认 - total获取失败");
            return null;
        }
        ljHouseInfo.setTotal(getTotal(total.text()));

        //房型
        Element houseType = redBig.get(1);
        ljHouseInfo.setHouseType(houseType.text());

        //面积
        Element area = redBig.get(2);

        ljHouseInfo.setArea(getArea(area.text()));
        //单价
        ljHouseInfo.setUnitPrice(ljHouseInfo.getTotal() / ljHouseInfo.getArea() * 10000);

        //建造年代
        Elements houseDesc = doc.select("ul.house_description");
        Elements shortDesc = houseDesc.select("li.short");
        if(CollectionUtils.isEmpty(shortDesc) || shortDesc.size() < 9){
            logger.error("[doAction]链家格式有改动，请确认 - short house desc获取失败 ");
            return null;
        }
        String builtYearStr = shortDesc.get(8).text();
        ljHouseInfo.setBuiltYear(getBuiltYear(builtYearStr));

        //小区
        Elements longDesc = houseDesc.select("li.long");
        if(CollectionUtils.isEmpty(longDesc)){
            logger.error("[doAction]链家格式有改动，请确认 - long house desc获取失败 ");
            return ljHouseInfo;
        }
        Elements communityEle = longDesc.select("li.arrow");
        String community = communityEle.select("a").text().substring(3);
        ljHouseInfo.setCommunity(community);


        Elements offShelfTag = doc.select("img.remove_tag");    //下架标识
        if(CollectionUtils.isNotEmpty(offShelfTag)){
            ljHouseInfo.setOffShelf(1);
        }

        return ljHouseInfo;
    }

    /**
     * 解析成效后的页面
     * <p>这种情况下，只取到total就行了</p>
     * @param ljHouseInfo
     * @param doc
     * @return
     */
    private LJHouseInfo parseDealed(LJHouseInfo ljHouseInfo, Document doc){
        logger.info("[parseDealed]");
        ljHouseInfo.setOffShelf(1);
        Elements redBig = doc.select("p.red");
        if(CollectionUtils.isEmpty(redBig) || redBig.size() != 2){
            logger.error("[doAction] 链家格式有改动，请确认 - red big 找不到");
            return null;
        }
        //总价
        Elements total = redBig.select("span[data-mark=price]");
        if(total.size() == 0){
            logger.error("[doAction] 链家格式有改动，请确认 - total获取失败");
            return null;
        }
        ljHouseInfo.setTotal(getTotal(total.text()));
        ljHouseInfo.setUnitPrice(getDealedUnitPrice(redBig.get(1).text()));
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

    private int getTotal(String total){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(total);
        if(m.find()) {
            return Integer.valueOf(m.group());
        }
        return 0;
    }

    private float getArea(String area) {
        Pattern p = Pattern.compile("\\d+\\.?\\d+");
        Matcher m = p.matcher(area);
        if(m.find()) {
            return Float.valueOf(m.group());
        }
        return 0.0f;
    }

    private int getBuiltYear(String builtYear){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(builtYear);
        if(m.find()) {
            return Integer.valueOf(m.group());
        }
        return 0;
    }

    /**
     * 获取交易完成后的单价
     * @param price
     * @return
     */
    public float getDealedUnitPrice(String price){
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(price);
        if(m.find()) {
            return Integer.valueOf(m.group());
        }
        return 0;
    }

    private String getImgs(Document doc){
        Elements picUl = doc.select(".pic_lists");
        if(picUl == null || picUl.size() <= 0) {
            return "";
        }
        String json = picUl.get(0).attr("data-info");
        try {
            JSONArray jsonArray = JSON.parseArray(json);
            if(CollectionUtils.isEmpty(jsonArray)) return "";
            List<String> urls = Lists.newArrayList();
            for(Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                String url = jsonObject.getString("url");
                if (StringUtils.isNotBlank(url)) {
                    urls.add(url);
                }
            }
            if(CollectionUtils.isNotEmpty(urls)) {
                return StringUtils.join(urls, ",");
            }
        }catch (Exception e){
            logger.error("[getImgs] Exception=#{}", e);
        }
        return "";
    }

    public static void main(String[] args) throws IOException {
        File input = new File("/Users/michealyang/Downloads/lianjia.html");
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
        LJMySpiderConvertorForMobile mobile = new LJMySpiderConvertorForMobile();
        System.out.println(mobile.getImgs(doc));

    }
}
