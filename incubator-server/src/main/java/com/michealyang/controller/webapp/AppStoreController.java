package com.michealyang.controller.webapp;

import com.michealyang.auth.domain.User;
import com.michealyang.auth.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author michealyang
 * @version 1.0
 * @created 17/7/23
 * 开始眼保健操： →_→  ↑_↑  ←_←  ↓_↓
 */
@Controller
@RequestMapping("/webapp/appstore")
public class AppStoreController {
    private static final Logger logger = LoggerFactory.getLogger(AppStoreController.class);

    @RequestMapping("/r/index")
    public Object appStore(HttpServletRequest request, HttpServletResponse response) {
        User user = UserUtil.getUser();
        logger.info("[appStore] user=#{}", user);
        return "/webapp/appstore";
    }
}
