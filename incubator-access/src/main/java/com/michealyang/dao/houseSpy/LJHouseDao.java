package com.michealyang.dao.houseSpy;

import com.michealyang.domain.houseSpy.LJHouseInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.InsertProvider;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by michealyang on 17/3/19.
 */
public interface LJHouseDao {

    String TABLE_NAME = "lj_house";

    @InsertProvider(type = SqlProvider.class, method = "insert")
    public int insert(LJHouseInfo houseInfo);

    class SqlProvider{
        public String insert(LJHouseInfo houseInfo) {
            BEGIN();

            INSERT_INTO(TABLE_NAME);
            if (StringUtils.isNotBlank(houseInfo.getHouseId())) {
                VALUES("house_id", "#{houseId}");
            }

            if (StringUtils.isNotBlank(houseInfo.getTitle())) {
                VALUES("title", "#{title}");
            }

            VALUES("total", "#{total}");
            VALUES("area", "#{area}");
            VALUES("unit_price", "#{unitPrice}");
            if(StringUtils.isNotBlank(houseInfo.getHouseType())) {
                VALUES("house_type", "#{houseType}");
            }
            if (StringUtils.isNotBlank(houseInfo.getCommunity())) {
                VALUES("community", "#{community}");
            }
            VALUES("built_year", "#{builtYear}");
            VALUES("off_shelf", "#{offShelf}");
            VALUES("ctime", "unix_timestamp()");

            return SQL();
        }
    }

}
