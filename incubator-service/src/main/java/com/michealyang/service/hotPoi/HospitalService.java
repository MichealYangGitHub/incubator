package com.michealyang.service.hotPoi;

import com.google.common.base.Preconditions;
import com.michealyang.dao.hotPoi.HospitalDao;
import com.michealyang.domain.hotPoi.Hospital;
import com.michealyang.dto.ResultDto;
import com.michealyang.util.Constants;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by michealyang on 17/3/3.
 */
@Service
public class HospitalService {

    private static final Logger logger = LoggerFactory.getLogger(HospitalService.class);

    private final static String LNG_LAT_PATTERN = "\\d{1,3}\\.?\\d{1,12},\\d{1,2}\\.?\\d{1,12}";    //经纬度正则匹配  116.453011,39.925985
    private final static Pattern pLngLat;
    static {
        pLngLat = Pattern.compile(LNG_LAT_PATTERN);
    }

    @Resource
    private HospitalDao hospitalDao;

    /**
     * 插入hospital数据
     * @param hospital  要插入的hospital数据
     * @return  成功返回true，失败返回false
     */
    public ResultDto insert(Hospital hospital){
        logger.info("[HospitalService.insert] hospital=#{}", hospital);
        Preconditions.checkArgument(hospital != null);

        if(!pLngLat.matcher(hospital.getLnglat().replace(" ", "")).matches()) {
            return new ResultDto(false, "经纬度格式错误");
        }
        String[] arr = hospital.getLnglat().split(",");
        String lng = arr[0];
        String lat = arr[1];
        if(Double.valueOf(lng) > 180 || Double.valueOf(lat) > 90) {
            return new ResultDto(false, "经纬度格式错误");
        }

        if(hospitalDao.insert(hospital) <= 0) {
            return new ResultDto(false, Constants.DB_FAILURE);
        }
        return new ResultDto(true, Constants.SUCCESS);
    }

    public List<Hospital> getHospitalByConds(Map<String, Object> conds){
        logger.info("[HospitalService.getHospitalByConds] conds=#{}", conds);
        Preconditions.checkArgument(MapUtils.isNotEmpty(conds));

        return hospitalDao.getHospitalsByConds(conds);
    }
}
