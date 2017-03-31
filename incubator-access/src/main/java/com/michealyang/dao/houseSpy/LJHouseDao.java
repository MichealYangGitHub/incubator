package com.michealyang.dao.houseSpy;

import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by michealyang on 17/3/19.
 */
public interface LJHouseDao {

    String TABLE_NAME = "lj_house";

    @Update("update " + TABLE_NAME + " set valid=valid where house_id=#{houseId}")
    public int lock(Long houseId);

    @SelectProvider(type = SqlProvider.class, method = "getHouses")
    public List<LJHouse> getHouses(HouseSpyQuery query);

    @SelectProvider(type = SqlProvider.class, method = "getHousesByConds")
    public List<LJHouse> getHousesByConds(Map<String, Object> conds);

    @InsertProvider(type = SqlProvider.class, method = "insert")
    public int insert(LJHouse houseInfo);

    class SqlProvider{

        public String getHousesByConds(Map<String, Object> conds) {
            BEGIN();
            SELECT("*");
            FROM(TABLE_NAME);
            if(MapUtils.isEmpty(conds)){
                WHERE("1=-1");
            }

            if(conds.get("houseId") != null){
                WHERE("house_id=#{houseId}");
            }

            String sql = SQL();
            if(conds.get("offset") != null && conds.get("pageSize") != null){
                sql += " limit #{offset}, #{pageSize}";
            }
            return sql;
        }

        public String getHouses(HouseSpyQuery query) {
            BEGIN();
            SELECT("*");
            FROM(TABLE_NAME);
            if(query == null){
                WHERE("1=-1");
            }

            ORDER_BY("id desc");

            String sql = SQL();
            sql += " limit #{offset}, #{pageSize}";
            return sql;
        }

        public String insert(LJHouse houseInfo) {
            BEGIN();

            INSERT_INTO(TABLE_NAME);
            if (houseInfo.getHouseId() != 0) {
                VALUES("house_id", "#{houseId}");
            }

            if (StringUtils.isNotBlank(houseInfo.getTitle())) {
                VALUES("title", "#{title}");
            }

            VALUES("area", "#{area}");
            if(StringUtils.isNotBlank(houseInfo.getHouseType())) {
                VALUES("house_type", "#{houseType}");
            }
            if (StringUtils.isNotBlank(houseInfo.getCommunity())) {
                VALUES("community", "#{community}");
            }
            if (StringUtils.isNotBlank(houseInfo.getUrl())) {
                VALUES("url", "#{url}");
            }

            if (StringUtils.isNotBlank(houseInfo.getImgs())) {
                VALUES("imgs", "#{imgs}");
            }

            VALUES("built_year", "#{builtYear}");
            VALUES("off_shelf", "#{offShelf}");
            VALUES("ctime", "unix_timestamp()");

            return SQL();
        }
    }

}
