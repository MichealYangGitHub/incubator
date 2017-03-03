/*
 * Copyright (c) 2016.
 */

package com.michealyang.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by michealyang on 16/12/6.
 */
@Controller
public class BaseController {
    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ResponseBody
    @RequestMapping("/")
    public String index(){
        logger.info("[BaseController.index] index");
        logger.error("[BaseController.index] index error log");
        return "welcome";
    }
}
