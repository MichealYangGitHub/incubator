package com.michealyang.controller;

import com.michealyang.model.base.TestUser;
import com.michealyang.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by michealyang on 15/12/3.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    //hello world
    @RequestMapping("/hello")
    public String helloWorld(){
        return "hello";
    }

    //jsp test
    @RequestMapping("/jsp")
    public String testJsp(Model model){
        model.addAttribute("name", "MichealYang");
        return "/test/test";
    }

    //mybatis test
    @ResponseBody
    @RequestMapping("/users")
    public String getUsers(){
        List<TestUser> userList = testService.getUsers();
        if(userList == null){
            return "Empty";
        }
        return userList.toString();
    }

    @ResponseBody
    @RequestMapping("/mybatisMap")
    public String mybatisMap(){
        List<TestUser> userList = testService.getUserByQuery();
        if(userList == null){
            return "Empty";
        }
        return userList.toString();
    }

    //freemarker test
    @RequestMapping("/freemarker")
    public String testFreemarker(Model model){
        model.addAttribute("name", "MichealYang");
        return "/test/test";
    }
}
