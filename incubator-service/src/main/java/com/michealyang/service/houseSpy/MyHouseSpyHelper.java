package com.michealyang.service.houseSpy;

import com.google.common.base.Preconditions;
import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.domain.LJHouseTrace;
import com.michealyang.model.houseSpy.dto.AgentTypeEnum;
import com.michealyang.model.houseSpy.dto.LJHouseInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by michealyang on 17/3/17.
 */
@Service
public class MyHouseSpyHelper {

    private static final Logger logger = LoggerFactory.getLogger(MyHouseSpyHelper.class);

    private final static String LJ_HOUSE_PATTERN = "^(http|https)://\\w{2,5}\\.lianjia\\.com/ershoufang/\\d+\\.html$";

    private final static String LJ_HOUSE_MOBILE_PATTERN = "^(http|https)://m.lianjia\\.com/\\w{2,5}/ershoufang/\\d+\\.html$";

    private final static Pattern ljHousePattern;

    private final static Pattern ljHouseMobilePattern;

    static {
        ljHousePattern = Pattern.compile(LJ_HOUSE_PATTERN);
        ljHouseMobilePattern = Pattern.compile(LJ_HOUSE_MOBILE_PATTERN);
    }

    /**
     * 根据url获得访问类型
     * <p>
     * <div>链家网页版链接示例：https://bj.lianjia.com/ershoufang/101101194178.html</div>
     * <div>链家移动版链接示例：https://m.lianjia.com/bj/ershoufang/101101194178.html</div>
     * </p>
     * @param url
     * @return
     */
    public AgentTypeEnum getAgentType(String url){
        logger.info("[getAgentType] url=#{}", url);
        if(!url.startsWith("http")){
            url = "https://" + url;
        }
        if(StringUtils.isBlank(url)){
            return AgentTypeEnum.INVALID;
        }
        if(ljHousePattern.matcher(url).matches()) {
            return AgentTypeEnum.WEB;
        }else if(ljHouseMobilePattern.matcher(url).matches()) {
            return AgentTypeEnum.MOBILE;
        }else {
            return AgentTypeEnum.INVALID;
        }
    }

    public LJHouse convertLJHouseInfo2LJHouse(LJHouseInfo ljHouseInfo){
        if(ljHouseInfo == null) return null;
        LJHouse ljHouse = new LJHouse();
        ljHouse.setHouseId(ljHouseInfo.getHouseId());
        ljHouse.setTitle(ljHouseInfo.getTitle());
        ljHouse.setArea(ljHouseInfo.getArea());
        ljHouse.setHouseType(ljHouseInfo.getHouseType());
        ljHouse.setCommunity(ljHouseInfo.getCommunity());
        ljHouse.setOffShelf(ljHouseInfo.getOffShelf());
        ljHouse.setBuiltYear(ljHouseInfo.getBuiltYear());
        ljHouse.setImgs(ljHouseInfo.getImgs());
        ljHouse.setUrl(ljHouseInfo.getUrl());
        return ljHouse;
    }

    public LJHouseTrace convertLJHouseInfo2LJHouseTrace(LJHouseInfo ljHouseInfo){
        if(ljHouseInfo == null) return null;
        LJHouseTrace ljHouseTrace = new LJHouseTrace();
        ljHouseTrace.setHouseId(ljHouseInfo.getHouseId());
        ljHouseTrace.setTotal(ljHouseInfo.getTotal());
        ljHouseTrace.setUnitPrice(ljHouseInfo.getUnitPrice());
        return ljHouseTrace;
    }

    /**
     * 根据链家url获得houseId
     * @param url   必须是链家url
     * @return
     */
    public long getLjHouseId(String url) {
        logger.info("[getLjHouseId] url=#{}", url);
        Preconditions.checkArgument(StringUtils.isNotBlank(url));
        Pattern p= Pattern.compile("\\d+");
        Matcher m=p.matcher(url);
        if(m.find()) {
            return Long.valueOf(m.group());
        }
        return 0l;
    }
}
