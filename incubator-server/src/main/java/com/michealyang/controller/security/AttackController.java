package com.michealyang.controller.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by michealyang on 17/3/28.
 */
@Controller
@RequestMapping("/security/attack")
public class AttackController {
    private static final Logger logger = LoggerFactory.getLogger(AttackController.class);

    @ResponseBody
    @RequestMapping("/imgRef")
    public Object imgRef() {
        logger.info("[imgRef]");
        return "http://www.51pptmoban.com/d/file/2015/11/27/2123f7686d354b4d1b67b99a7f657747.jpg";
    }
}
