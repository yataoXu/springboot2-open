package cn.myframe.controller;

import cn.myframe.entity.PreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/24/024 21:57
 */
@RestController
public class MvcController {

    @Autowired
    private PreUser preUser;

    @RequestMapping(value = "/preUser")
    public PreUser preUser(){
        return preUser;
    }

}
