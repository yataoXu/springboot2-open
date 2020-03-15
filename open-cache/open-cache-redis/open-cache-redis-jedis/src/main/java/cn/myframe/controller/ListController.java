package cn.myframe.controller;

import cn.myframe.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * https://blog.csdn.net/XinhuaShuDiao/article/details/84906382
 */
@RestController
public class ListController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    JedisUtils jedisUtils;

    @RequestMapping("/list/blpop")
    public String blpop(){
       String data = redisTemplate.opsForList().rightPop("list",20,TimeUnit.SECONDS);
       return data;
    }

    @RequestMapping("/list/push/{data}")
    public String push(@PathVariable String data){
        redisTemplate.opsForList().rightPush("list",data);
        return "push";
    }
}
