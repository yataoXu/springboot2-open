package cn.myframe.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 15:08
 * @Version 1.0
 */
@RestController
public class HelloOath2Controller {

    @RequestMapping("/api/hello/{id}")
    public String helloOath2(@PathVariable long id) {
        System.out.println("请求的ID编码为：" + id);
        return "helloOath2";
    }

    @RequestMapping("/api2/hello/{id}")
    public String hello2Oath2(@PathVariable long id) {
        System.out.println("请求的ID编码为：" + id);
        return "hello2Oath2";
    }

}
