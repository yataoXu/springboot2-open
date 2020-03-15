package cn.myframe.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: ynz
 * @Date: 2018/12/26/026 14:31
 * @Version 1.0
 */
@RestController
public class PermController {

    @RequestMapping("/sysRead")
    @RequiresPermissions("sys:read")
    public String sysRead(){
        return "you can sysRead";
    }

    @RequiresPermissions("sys:write")
    @RequestMapping("/sysWrite")
    public String sysWrite(){
        return "you can sysWrite";
    }

    @RequiresPermissions("sys:delete")
    @RequestMapping("/sysDelete")
    public String sysDel(){
        return "you can sysDelete";
    }
}
