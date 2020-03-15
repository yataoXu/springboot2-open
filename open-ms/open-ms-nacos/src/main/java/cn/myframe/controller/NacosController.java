package cn.myframe.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/3/14/014 16:44
 * @Version 1.0
 */
@RestController
public class NacosController {

    @NacosValue(value = "${useLocalCache:defult}", autoRefreshed = true)
    private String useLocalCache;

    @RequestMapping(value = "/get")
    @ResponseBody
    public String get() {
        return useLocalCache;
    }
}
