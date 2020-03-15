package cn.myframe.controller;

import cn.myframe.service.LuaScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/2/20/020 11:14
 * @Version 1.0
 */
@RestController
public class LuaLimitController {

    @Autowired
    LuaScriptService luaScriptService;

    @RequestMapping("/limit")
    public String limit(){
        luaScriptService.redisAddScriptExec();
        return "success";
    }

    @RequestMapping("/limit2")
    public String limit2(){
        return "success";
    }


}
