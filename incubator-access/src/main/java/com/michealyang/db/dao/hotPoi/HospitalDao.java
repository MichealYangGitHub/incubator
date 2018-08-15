package com.michealyang.db.dao.hotPoi;

import com.michealyang.model.hotPoi.domain.Hospital;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by michealyang on 17/3/3.
 */
public interface HospitalDao {

    final String TABLE_NAME = "hotpoi_hospital";

    @InsertProvider(type = SqlProvider.class, method = "insert")
    int insert(Hospital hospital);

    @SelectProvider(type = SqlProvider.class, method = "getHospitalsByConds")
    List<Hospital> getHospitalsByConds(Map<String, Object> conds);

    class SqlProvider{

        public String getHospitalsByConds(Map<String, Object> conds) {
            BEGIN();
            SELECT("*");
            FROM(TABLE_NAME);
            if(conds.get("name") != null){
                WHERE("name like \"%\"#{name}\"%\"");
            }

            if(conds.get("alias") != null){
                WHERE("alias like \"%\"#{alias}\"%\"");
            }

            if(conds.get("level") != null && conds.get("level") != 0){
                WHERE("level=#{level}");
            }

            return SQL();
        }

        public String insert(Hospital hospital){
            BEGIN();
            INSERT_INTO(TABLE_NAME);

            if (StringUtils.isNotBlank(hospital.getName())) {
                VALUES("name", "#{name}");
            }

            if (StringUtils.isNotBlank(hospital.getAlias())) {
                VALUES("alias", "#{alias}");
            }

            VALUES("level", "#{level}");

            if (StringUtils.isNotBlank(hospital.getLocation())) {
                VALUES("location", "#{location}");
            }

            if (StringUtils.isNotBlank(hospital.getLnglat())) {
                VALUES("lnglat", "#{lnglat}");
            }

            VALUES("ctime", "unix_timestamp()");

            return SQL();
        }
    }
}
