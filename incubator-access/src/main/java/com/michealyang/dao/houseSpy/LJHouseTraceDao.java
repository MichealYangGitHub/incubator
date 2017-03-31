package com.michealyang.dao.houseSpy;

import com.michealyang.model.houseSpy.domain.LJHouseTrace;
import org.apache.commons.collections4.MapUtils;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by michealyang on 17/3/19.
 */
public interface LJHouseTraceDao {

    String TABLE_NAME = "lj_house_trace";

//    @SelectProvider(type = SqlProvider.class, method = "getHouseInfos")
//    public List<LJHouseInfo> getHouseInfos(HouseSpyQuery query);

    @SelectProvider(type = SqlProvider.class, method = "getHouseTracesByConds")
    public List<LJHouseTrace> getHouseTracesByConds(Map<String, Object> conds);

    @InsertProvider(type = SqlProvider.class, method = "insert")
    public int insert(LJHouseTrace ljHouseTrace);

    class SqlProvider{

        public String getHouseTracesByConds(Map<String, Object> conds) {
            BEGIN();
            SELECT("*");
            FROM(TABLE_NAME);
            if(MapUtils.isEmpty(conds)){
                WHERE("1=-1");
            }

            if(conds.get("houseIds") != null && conds.get("houseIds") instanceof List){
                List<Long> ids = (List<Long>)conds.get("houseIds");
                String houseIds = "-1";
                if(!CollectionUtils.isEmpty(ids)){
                    List<String> placeHolder = new ArrayList<String>();
                    for (int i = 0; i < ids.size(); i++) {
                        if(ids.get(i) == null) continue;
                        placeHolder.add("'" + String.valueOf(ids.get(i)) + "'");
                    }
                    houseIds = StringUtils.join(placeHolder, ",");
                }
                WHERE("house_id in (" + houseIds + ")");
            }

            if(conds.get("startTime") != null) {
                String startTime = (String) conds.get("startTime");
                if(StringUtils.isNotBlank(startTime)){
                    WHERE("ctime > unix_timestamp(#{startTime})");
                }
            }

            if(conds.get("endTime") != null) {
                String endTime = (String) conds.get("endTime");
                if(StringUtils.isNotBlank(endTime)){
                    WHERE("ctime < unix_timestamp(#{endTime})");
                }
            }

            ORDER_BY("id desc");

            return SQL();
        }

        public String insert(LJHouseTrace ljHouseTrace) {
            BEGIN();

            INSERT_INTO(TABLE_NAME);
            if (ljHouseTrace.getHouseId() != null && ljHouseTrace.getHouseId() != 0) {
                VALUES("house_id", "#{houseId}");
            }

            if (ljHouseTrace.getTotal() != null && ljHouseTrace.getTotal() != 0) {
                VALUES("total", "#{total}");
            }

            if(ljHouseTrace.getUnitPrice() != null){
                VALUES("unit_price", "#{unitPrice}");
            }

            if(ljHouseTrace.getCtime() == null) {
                VALUES("ctime", "unix_timestamp()");
            }else{
                VALUES("ctime", "#{ctime}");
            }

            return SQL();
        }
    }

}
