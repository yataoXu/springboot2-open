package cn.myframe.controller;

import cn.myframe.cache.CacheUtil;
import cn.myframe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 8:13
 */
@RestController
public class CacheController {

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    User user;

    @RequestMapping("/getCache")
    public String getCache(){
        cacheUtil.getUser(user);
        return "success";
    }
}
