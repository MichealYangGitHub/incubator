/*
 * Copyright (c) 2016.
 */

package com.michealyang.controller;

import com.michealyang.sso.access.model.User;
import com.michealyang.sso.client.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by michealyang on 16/12/6.
 */
@Controller
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping("/")
    public String index(Model model){
        logger.info("[index] index");
        User user = UserUtil.getUser();
        logger.info("[index] user=#{}", user);

        model.addAttribute("user", user);

        return "index";
    }

    @RequestMapping("/error/404")
    public String index404(Model model){
        logger.info("[index404]");
        return "404";
    }

    @RequestMapping("/error/500")
    public String index500(Model model){
        logger.info("[index500]");
        return "500";
    }
}
