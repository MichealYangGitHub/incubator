package com.michealyang.controller.houseSpy;

import com.michealyang.domain.houseSpy.HouseSpyQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by michealyang on 17/3/21.
 */
@Controller
@RequestMapping("/houseSpy/lianjia/")
public class LJHouseSpyController {

    private static final Logger logger = LoggerFactory.getLogger(LJHouseSpyController.class);

    @RequestMapping("/r/index")
    public String index(Model model) {
        logger.info("[index]");
        return "/houseSpy/lianjia";
    }

    /**
     * 根据查询条件展示结果
     * <p>不限制是否登录，不限制是否关注</p>
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping("/r/list")
    public Object list(HouseSpyQuery query) {
        return null;
    }

    /**
     * 根据查询条件展示结果
     * <p>需要登录，只展示关注的结果</p>
     * @param query 查询条件
     * @return
     */
    @ResponseBody
    @RequestMapping("/r/myList")
    public Object myList(HouseSpyQuery query) {
        return null;
    }

    /**
     * 添加一个关注
     * <p>需要登录</p>
     * @param url
     * @return
     */
    @ResponseBody
    @RequestMapping("/w/addConcern")
    public Object addConcern(String url) {
        return null;
    }
}
