package cn.myframe.controller;

import cn.myframe.entity.PreUser;
import cn.myframe.entity.User;
import cn.myframe.entity.ValueModel;
import cn.myframe.profile.Person;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/24/024 17:21
 * @Version 1.0
 */
@RestController
@Slf4j
public class ConfigController {

    @Autowired
    private User user;

    @Autowired
    private PreUser preUser;

    @Autowired
    private ValueModel valueModel;

    @Autowired
    private Person person;

    @RequestMapping("/user")
    public String user(){
        log.info(JSON.toJSONString(user));
        log.info(JSON.toJSONString(preUser));
        valueModel.outputResource();
        person.say();
        return "";
    }
}
