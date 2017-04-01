package com.michealyang.dao.houseSpy;

import com.michealyang.model.houseSpy.domain.LJHouse;
import com.michealyang.model.houseSpy.dto.HouseSpyQuery;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;

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

    @Deprecated
    @SelectProvider(type = SqlProvider.class, method = "getHouses")
    public List<LJHouse> getHouses(HouseSpyQuery query);

    @SelectProvider(type = SqlProvider.class, method = "getHousesByConds")
    public List<LJHouse> getHousesByConds(Map<String, Object> conds);

    @Select("select house_id from " + TABLE_NAME)
    public List<Long> getAllHouseIds();

    @Select("select house_id from " + TABLE_NAME + " where off_shelf=0")
    public List<Long> getAllOnShelfHouseIds();

    @Select("select url from " + TABLE_NAME)
    public List<String> getAllUrls();

    @Select("select url from " + TABLE_NAME + " where off_shelf=0")
    public List<String> getAllOnShelfUrls();

    @Update("update " + TABLE_NAME + " set final_total=#{finalTotal}, off_shelf=1 where house_id=#{houseId}")
    public int offShelfHouse(@Param("houseId") long houseId, @Param("finalTotal") int finalTotal);

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

            if(conds.get("community") != null){
                WHERE("community like \"%\"#{community}\"%\"");
            }

            ORDER_BY("id desc");

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

        public String insert(LJHouse ljHouse) {
            BEGIN();

            INSERT_INTO(TABLE_NAME);
            if (ljHouse.getHouseId() != 0) {
                VALUES("house_id", "#{houseId}");
            }

            if (StringUtils.isNotBlank(ljHouse.getTitle())) {
                VALUES("title", "#{title}");
            }

            if(ljHouse.getFinalTotal() != 0){
                VALUES("final_total", "#{finalTotal}");
            }

            VALUES("area", "#{area}");
            if(StringUtils.isNotBlank(ljHouse.getHouseType())) {
                VALUES("house_type", "#{houseType}");
            }
            if (StringUtils.isNotBlank(ljHouse.getCommunity())) {
                VALUES("community", "#{community}");
            }
            if (StringUtils.isNotBlank(ljHouse.getUrl())) {
                VALUES("url", "#{url}");
            }

            if (StringUtils.isNotBlank(ljHouse.getImgs())) {
                VALUES("imgs", "#{imgs}");
            }

            VALUES("built_year", "#{builtYear}");
            VALUES("off_shelf", "#{offShelf}");
            VALUES("ctime", "unix_timestamp()");

            return SQL();
        }
    }

}
