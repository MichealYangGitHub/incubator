package com.michealyang.controller.hotPoi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by michealyang on 17/3/5.
 */
@Controller
@RequestMapping("/hotPoi")
public class HotPoiController {

    private static final Logger logger = LoggerFactory.getLogger(HotPoiController.class);

    @RequestMapping("/index")
    public String index(Model model) {
        logger.info("[HotPoiController.index]");
        return "/hotPoi/index";
    }
}
