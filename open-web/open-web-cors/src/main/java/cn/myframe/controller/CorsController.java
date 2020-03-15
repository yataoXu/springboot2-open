package cn.myframe.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 11:12
 * @Version 1.0
 */
@RestController
//@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
public class CorsController {

    @RequestMapping("/preUser")
    @CrossOrigin()
    public Map<String,String> preUser(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("preUser","success");
        return map;
    }

   @RequestMapping("/hello")
    public String index(HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        return "Hello World";
    }
}
