package cn.myframe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MvcController {

    /* 对应 http://127.0.0.1:7091/sayHi/tom */
    @RequestMapping("/sayHi/{name}")
    public String sayHi(@PathVariable("name")String name ){
        return "hi!"+ name;
    }
     /*对应 http://127.0.0.1:7091/sayHello?name=tom */
    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam String name){
        return "你好!"+name;
    }

    /* 对应POST http://127.0.0.1:7091/sayGood 消息体为tom */
    @RequestMapping(value = "/sayGood",method = {RequestMethod.POST})
    public String sayGood(@RequestBody(required = false) String name){
        return "find!"+name;
    }


}
