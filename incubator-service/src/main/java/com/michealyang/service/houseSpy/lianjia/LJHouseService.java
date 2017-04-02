package com.michealyang.service.houseSpy.lianjia;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.michealyang.dao.houseSpy.LJHouseDao;
import com.michealyang.dao.houseSpy.LJHouseTraceDao;
import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.domain.LJHouseTrace;
import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import com.michealyang.model.houseSpy.dto.LJHouseInfoDto;
import com.michealyang.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by michealyang on 17/3/21.
 */
@Service
public class LJHouseService {
    private static final Logger logger = LoggerFactory.getLogger(LJHouseService.class);

    @Resource
    private LJHouseDao ljHouseDao;

    @Resource
    private LJHouseTraceDao ljHouseTraceDao;

    public boolean checkExist(Long houseId) {
        logger.info("[checkExist] houseId=#{}", houseId);
        Preconditions.checkArgument(houseId != null && houseId != 0);
        if(ljHouseDao.lock(houseId) > 0) return true;
        return false;
    }

    public List<Long> getAllHouseIds() {
        return ljHouseDao.getAllHouseIds();
    }

    public List<String> getAllUrls() {
        return ljHouseDao.getAllUrls();
    }

    public List<Long> getAllOnShelfHouseIds(){
        return ljHouseDao.getAllOnShelfHouseIds();
    }

    public List<String> getAllOnShelfUrls() {
        return ljHouseDao.getAllOnShelfUrls();
    }

    public List<LJHouseInfoDto> getHouseInfos(HouseSpyQuery query) {
        logger.info("[getHouseInfos] query=#{}", query);
        initQuery(query);
        logger.info("[getHouseInfos] query after init=#{}", query);

        Map<String, Object> conds = Maps.newHashMap();
        conds.put("houseId", query.getHouseId());
//        conds.put("startTime", query.getStartTime());
//        conds.put("endTime", query.getEndTime());
        conds.put("community", query.getCommunity());
        conds.put("offset", query.getOffset());
        conds.put("pageSize", query.getPageSize());

        List<LJHouse> ljHouses = ljHouseDao.getHousesByConds(conds);

//        List<LJHouse> ljHouses = ljHouseDao.getHouses(query);

        return fillHouseTrace(ljHouses, query);
    }

    public int getHouseCount(HouseSpyQuery query){
        logger.info("[getHouseCount] query=#{}", query);
        initQuery(query);
        logger.info("[getHouseCount] query after init=#{}", query);

        Map<String, Object> conds = Maps.newHashMap();
        conds.put("houseId", query.getHouseId());
//        conds.put("startTime", query.getStartTime());
//        conds.put("endTime", query.getEndTime());
        conds.put("community", query.getCommunity());
        conds.put("offset", query.getOffset());
        conds.put("pageSize", query.getPageSize());
        return ljHouseDao.getHouseCountByConds(conds);
    }


    /**
     * 根据LJHouse信息，查询其对应的Trace信息
     * <p>一个LJHouse可能对应很多个LJHouseTrace。因此要做映射，映射成LJHouseInfoDto</p>
     * @param ljHouses
     * @param query 查询条件
     * @return
     */
    private List<LJHouseInfoDto> fillHouseTrace(List<LJHouse> ljHouses, HouseSpyQuery query){
        if(CollectionUtils.isEmpty(ljHouses)) return Collections.emptyList();

        List<Long> houseIds = Lists.newArrayList();
        for(LJHouse ljHouse : ljHouses){
            if(ljHouse == null) continue;
            houseIds.add(ljHouse.getHouseId());
        }

        Map<String, Object> conds = Maps.newHashMap();
        conds.put("houseIds", houseIds);
        conds.put("startTime", query.getStartTime());
        conds.put("endTime", query.getEndTime());
        List<LJHouseTrace> ljHouseTraces = ljHouseTraceDao.getHouseTracesByConds(conds);

        return packageHouseInfo(ljHouses, ljHouseTraces, query.getRange());
    }

    /**
     * 将LJHouse和LJHouseTrace按照houseId打包成统一的LJHouseInfoDto数据
     * <p>组装的LJHouseTrace是长度为range的一个List</p>
     * @param ljHouses
     * @param ljHouseTraces
     * @param range
     * @return
     */
    private List<LJHouseInfoDto> packageHouseInfo(List<LJHouse> ljHouses,
                                                  List<LJHouseTrace> ljHouseTraces,
                                                  int range) {
        if(CollectionUtils.isEmpty(ljHouses) || CollectionUtils.isEmpty(ljHouseTraces)) return Collections.emptyList();
        Map<Long, List<LJHouseTrace>> houseTraceMap = mappingHouseTrace(ljHouseTraces);
        List<LJHouseInfoDto> ljHouseInfoDtos = Lists.newArrayList();
        for(LJHouse ljHouse : ljHouses){
            if(ljHouse == null) continue;
            LJHouseInfoDto ljHouseInfoDto = new LJHouseInfoDto();
            ljHouseInfoDto.setLjHouse(ljHouse);
            List<LJHouseTrace> tmp = houseTraceMap.get(ljHouse.getHouseId());
            //存储经过hash后的traces
            ljHouseInfoDto.setLjHouseTraces(hashHouseTrace(ljHouse, tmp, range));
            ljHouseInfoDto.setTimeSpan(DateUtil.minusDaysArray(DateUtil.today(), range - 1));
            ljHouseInfoDtos.add(ljHouseInfoDto);
        }
        return ljHouseInfoDtos;
    }

    /**
     * 将List<LJHouseTrace>中所有数据，按照houseId为key，与之对应的LJHouseTraces为value，映射成map
     * @param ljHouseTraces
     * @return
     */
    private Map<Long, List<LJHouseTrace>> mappingHouseTrace(List<LJHouseTrace> ljHouseTraces){
        if(CollectionUtils.isEmpty(ljHouseTraces)) return Collections.emptyMap();
        Map<Long, List<LJHouseTrace>> houseTraceMap = Maps.newHashMap();
        for(LJHouseTrace ljHouseTrace : ljHouseTraces){
            if(ljHouseTrace == null) continue;
            List<LJHouseTrace> tmp = houseTraceMap.get(ljHouseTrace.getHouseId());
            if(tmp == null) {
                tmp = Lists.newArrayList();
                tmp.add(ljHouseTrace);
                houseTraceMap.put(ljHouseTrace.getHouseId(), tmp);
            }else{
                tmp.add(ljHouseTrace);
            }
        }
        return houseTraceMap;
    }

    /**
     * 将LJHouseTrace按照range进行hash处理
     * <p>Hash原则：
     * 1. 按照日期(精确到天)进行Hash
     * 2. 同一天的，选取最后一次(指时间)价格做为最终值
     * 3. 最后结果list长度与range相同。如range为30，list长度也应该为30，没有数据的设置为0
     * </p>
     * @param ljHouse 需要知道该房源是否已经下架。如果房源已下架，下架以后没有Trace数据，需要用finalTotal来补充
     * @param ljHouseTraces 需要保证都是同一个houseId的数据
     * @param range 时间范围，如30天，60天
     * @return
     */
    private List<LJHouseTrace> hashHouseTrace(LJHouse ljHouse, List<LJHouseTrace> ljHouseTraces, int range){
        Preconditions.checkArgument(ljHouse != null);
        if(CollectionUtils.isEmpty(ljHouseTraces)) return ljHouseTraces;

        List<LJHouseTrace> hashedLjHouseTraces = Lists.newArrayListWithExpectedSize(range);
        String today = DateUtil.today();
        List<String> daysArray = DateUtil.minusDaysArray(today, range - 1);

        Map<String, LJHouseTrace> dateTraceMap = Maps.newHashMap();

        for(LJHouseTrace ljHouseTrace : ljHouseTraces){
            if(ljHouseTrace == null) continue;
            String ctime = DateUtil.UT2Day(ljHouseTrace.getCtime());
            //由于是按时间倒序，因此先遍历的数据会是最新的数据
            if(dateTraceMap.get(ctime) == null) {
                dateTraceMap.put(ctime, ljHouseTrace);
            }
        }

        for(String day : daysArray) {
            hashedLjHouseTraces.add(dateTraceMap.get(day));
        }

        //如果已经下架，则把后面没有爬取的数据填充为finalTotal数据
        if(ljHouse.getOffShelf() != 0) {
            for (int i = daysArray.size() - 1; i >= 0; i--) {
                LJHouseTrace ljHouseTrace = hashedLjHouseTraces.get(i);
                if(ljHouseTrace != null){
                    break;
                }
                ljHouseTrace = new LJHouseTrace();
                ljHouseTrace.setHouseId(ljHouse.getHouseId());
                ljHouseTrace.setTotal(ljHouse.getFinalTotal());
                ljHouseTrace.setUnitPrice(ljHouse.getFinalTotal() / ljHouse.getArea());
                hashedLjHouseTraces.set(i, ljHouseTrace);
            }
        }

        return hashedLjHouseTraces;
    }

    /**
     * 初始化查询数据
     * @param query
     */
    private void initQuery(HouseSpyQuery query){
        if(query == null) query = new HouseSpyQuery();
        if(query.getPageNum() == null || query.getPageNum() <= 0) {
            query.setPageNum(1);
        }
        if (query.getPageSize() == null || query.getPageSize() <= 0) {
            query.setPageSize(10);
        }
        query.setOffset((query.getPageNum() - 1) * query.getPageSize());

        //默认查询30内的数据
        if(query.getRange() == null || query.getRange() <= 0){
            query.setRange(30);
        }
        String today = DateUtil.today();
        query.setEndTime(today + " 23:59:59");
        query.setStartTime(DateUtil.minusDays(today, query.getRange() - 1) + " 00:00:00");
    }
}
