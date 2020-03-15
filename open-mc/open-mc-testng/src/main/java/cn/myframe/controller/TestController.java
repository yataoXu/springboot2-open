package cn.myframe.controller;

import cn.myframe.test.UserCaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/5/11/011 11:36
 * @Version 1.0
 */
@RestController
public class TestController {

    @Autowired
    UserCaseTest userCaseTest;

    @RequestMapping("/exec")
    public String execTestCast(){
        userCaseTest.say();
        return "success";

    }
}
