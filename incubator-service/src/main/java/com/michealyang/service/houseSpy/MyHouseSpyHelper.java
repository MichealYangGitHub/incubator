package com.michealyang.service.houseSpy;

import com.michealyang.dto.AgentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public static AgentTypeEnum getAgentType(String url){
        if(ljHousePattern.matcher(url).matches()) {
            return AgentTypeEnum.WEB;
        }else if(ljHouseMobilePattern.matcher(url).matches()) {
            return AgentTypeEnum.MOBILE;
        }else {
            return AgentTypeEnum.INVALID;
        }
    }
}
