package com.michealyang.service;

import com.michealyang.db.dao.TestDao;
import com.michealyang.model.base.TestUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michealyang on 16/1/24.
 */
@Service
public class TestService {

    @Autowired
    private TestDao testDao;

    public List<TestUser> getUsers(){
        return testDao.getUsers();
    }

    public List<TestUser> getUserByQuery(){
        Map<String, Object> cond = new HashMap<String, Object>();
        cond.put("id", 1l);
        return testDao.getUserByCond(cond);
    }
}