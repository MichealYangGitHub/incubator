package com.michealyang.dao;

import com.michealyang.model.TestUser;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

/**
 * Created by michealyang on 16/1/24.
 */
@Repository("testDao")
public interface TestDao {
    @Select("select id, name, passwd, ctime from user")
    public List<TestUser> getUsers();

    @SelectProvider(type=SqlProvider.class, method = "getUserByCond")
    public List<TestUser> getUserByCond(Map<String, Object> cond);

    static class SqlProvider{
        public String getUserByCond(Map<String, Object> cond){
            Long id = (Long)cond.get("id");
            String name = (String)cond.get("name");
            BEGIN();
            SELECT("id, name, passwd, ctime");
            FROM("user");
            if(id != null){
                WHERE("id=#{id}");
            }
            if(name != null){
                WHERE("name like \"%\"#{email}\"%\"");
            }
            String sql = SQL();
            return sql;
        }
    }
}
