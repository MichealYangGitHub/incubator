package com.michealyang.controller.houseSpy;

import com.michealyang.model.base.dto.PageDto;
import com.michealyang.model.base.dto.ResultDto;
import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import com.michealyang.model.houseSpy.dto.LJHouseInfoDto;
import com.michealyang.service.houseSpy.lianjia.LJHouseService;
import com.michealyang.service.schedule.SpyAction;
import com.michealyang.util.Constants;
import com.michealyang.util.JsonResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by michealyang on 17/3/21.
 */
@Controller
@RequestMapping("/houseSpy/lianjia/")
public class LJHouseSpyController {

    private static final Logger logger = LoggerFactory.getLogger(LJHouseSpyController.class);

    @Resource
    private LJHouseService ljHouseService;

    @Resource
    private SpyAction spyAction;

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
        logger.info("[list] query=#{}", query);
        List<LJHouseInfoDto> ljHouseInfoDtos = ljHouseService.getHouseInfos(query);
        int totalSize = ljHouseService.getHouseCount(query);

        PageDto pageDto = new PageDto<List>();
        pageDto.setData(ljHouseInfoDtos);
        pageDto.setTotalSize(totalSize);
        pageDto.setPageNums(totalSize % query.getPageSize() == 0 ? totalSize / query.getPageSize() : totalSize / query.getPageSize() + 1);

        return JsonResponseUtil.successResp(Constants.SUCCESS, pageDto);
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
    @RequestMapping("/w/addSpy")
    public Object addSpy(String url) {
        logger.info("[addSpy] url=#{}", url);
        ResultDto resultDto = ljHouseService.addSpy(url);
        if(!resultDto.isSuccess()) {
            return JsonResponseUtil.failureResp(resultDto.getMsg(), null);
        }
        return JsonResponseUtil.successResp(resultDto.getMsg(), null);
    }

    @ResponseBody
    @RequestMapping("/admin/spyGo")
    public Object spyGo(){
        logger.info("[spyGo] 强制开始爬取任务");
        try {
            spyAction.go();
            return JsonResponseUtil.successResp(Constants.SUCCESS, null);
        } catch (InterruptedException e) {
            logger.info("[spyGo] Exception=#{}", e);
            return JsonResponseUtil.successResp(Constants.FAILURE, e.getMessage());
        }
    }
}
