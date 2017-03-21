package com.michealyang.service.houseSpy.lianjia.processor;

import com.google.common.base.Preconditions;
import com.michealyang.domain.houseSpy.LJHouseInfo;
import com.michealyang.domain.houseSpy.mySpider.MyDocument;
import com.michealyang.domain.houseSpy.mySpider.MyResponse;
import com.michealyang.service.houseSpy.IConvertor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        ljHouseInfo.setHouseId(getHouseId(data.getUrl()));
        Document doc = myDocument.getJsoupDoucument();

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

        //房型
        Element houseType = redBig.get(1);
        ljHouseInfo.setHouseType(houseType.text());

        //面积
        Element area = redBig.get(2);

        ljHouseInfo.setTitle(myDocument.getTitle() == null ? "" : myDocument.getTitle());
        ljHouseInfo.setArea(getArea(area.text()));
        ljHouseInfo.setTotal(getTotal(total.text()));
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

        //是否下架
        Elements offShelfTag = doc.select("img.remove_tag");
        if(CollectionUtils.isNotEmpty(offShelfTag)){
            ljHouseInfo.setOffShelf(1);
        }

        return ljHouseInfo;
    }

    private String getHouseId(String url) {
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Pattern p= Pattern.compile("\\d+");
        Matcher m=p.matcher(url);
        if(m.find()) {
            return m.group();
        }
        return "";
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
}
