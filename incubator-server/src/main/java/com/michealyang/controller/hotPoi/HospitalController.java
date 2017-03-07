package com.michealyang.controller.hotPoi;

import com.google.common.collect.Maps;
import com.michealyang.domain.hotPoi.Hospital;
import com.michealyang.dto.ResultDto;
import com.michealyang.service.hotPoi.HospitalService;
import com.michealyang.util.Constants;
import com.michealyang.util.JsonResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by michealyang on 17/3/3.
 */
@Controller
@RequestMapping("/hotPoi/hospital")
public class HospitalController {

    private static final Logger logger = LoggerFactory.getLogger(HospitalController.class);

    @Resource
    private HospitalService hospitalService;

    @RequestMapping("/r/index")
    public String hospital(Model model) {
        logger.info("[HospitalController.hospital]");
        return "/hotPoi/hospital";
    }

    @RequestMapping("/r/addView")
    public String addView(Model model){
        logger.info("[HospitalController.addView]");
        return "/hotPoi/addHospital";
    }

    @ResponseBody
    @RequestMapping("/r/hospitals")
    public Object getHospitals(Integer level) {
        logger.info("[HospitalController.getHospitals] level=#{}", level);

        Map<String, Object> conds = Maps.newHashMap();
        if(null != level) {
            conds.put("level", level);
        }

        List<Hospital> hospitals = hospitalService.getHospitalByConds(conds);

        return JsonResponseUtil.successResp(Constants.SUCCESS, hospitals);
    }

    @ResponseBody
    @RequestMapping("/w/add")
    public Object addHospitals(Hospital hospital){
        logger.info("[HospitalController.addHospitals] hospital=#{}", hospital);
        if(hospital == null
                || (StringUtils.isEmpty(hospital.getName()) && StringUtils.isEmpty(hospital.getAlias()))
                || StringUtils.isEmpty(hospital.getLnglat())) {
            return JsonResponseUtil.failureResp("关键信息为空", null);
        }
        ResultDto resultDto = hospitalService.insert(hospital);
        if(!resultDto.isSuccess()){
            return JsonResponseUtil.failureResp(resultDto.getMsg(), null);
        }
        return JsonResponseUtil.successResp(Constants.SUCCESS, null);
    }
}
