package com.michealyang.controller.houseSpy;

import com.michealyang.model.base.dto.ResultDto;
import com.michealyang.model.houseSpy.dto.AgentTypeEnum;
import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import com.michealyang.model.houseSpy.dto.LJHouseInfoDto;
import com.michealyang.service.houseSpy.MyHouseSpyHelper;
import com.michealyang.service.houseSpy.lianjia.LJHouseService;
import com.michealyang.service.houseSpy.lianjia.LJHouseSpy;
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
    private LJHouseSpy ljHouseSpy;

    @Resource
    private MyHouseSpyHelper myHouseSpyHelper;

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

        return JsonResponseUtil.successResp(Constants.SUCCESS, ljHouseInfoDtos);
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
        AgentTypeEnum agentTypeEnum = myHouseSpyHelper.getAgentType(url);
        if(AgentTypeEnum.INVALID.equals(agentTypeEnum)){
            return JsonResponseUtil.failureResp("URL格式不正确", null);
        }

        Long houseId = myHouseSpyHelper.getLjHouseId(url);
        if(houseId == 0){
            return JsonResponseUtil.failureResp("URL格式不正确", null);
        }
        logger.info("[addSpy] houseId=#{}", houseId);

        if(ljHouseService.checkExist(houseId)){
            logger.info("[addSpy] houseId=#{}已经在监控中", houseId);
            //已经存在, do nothing
            return JsonResponseUtil.successResp(Constants.SUCCESS, null);
        }else{
            //不存在，则立即爬取一次数据
            logger.info("[addSpy] houseId=#{}不在监控中，开始添加监控", houseId);
            ResultDto resultDto = ljHouseSpy.crawlOneHouse(url);
            if(!resultDto.isSuccess()) {
                return JsonResponseUtil.failureResp(resultDto.getMsg(), null);
            }
            return JsonResponseUtil.successResp(Constants.SUCCESS, null);
        }
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
