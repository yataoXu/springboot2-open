package cn.myframe.controller;

import cn.myframe.config.RedisObject;
import cn.myframe.utils.JedisUtil;
import cn.myframe.utils.JedisUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/11/011 9:35
 * @Version 1.0
 */
@RestController
public class RedisController {

    @Resource
    private RedisTemplate<String, RedisObject> redisTemplate;


    @Autowired
    JedisUtils jedisUtil;


    @GetMapping("/set/{key}/{value}")
    public String setValue(@PathVariable String key, @PathVariable String value){
        //保存对象
        redisTemplate.opsForValue().set(key,new RedisObject(value));
        //获取对象
        RedisObject object = redisTemplate.opsForValue().get(key);
        System.out.println("6");

        return JSON.toJSONString(object);
    }

    @GetMapping("/jedis")
    public String jedis(){
        Jedis jedis = JedisUtil.getJedisClient("10.10.2.139",7000, "");
        System.out.println( jedis.info());
        List<String> list = jedis.configGet("*");
        System.out.println(list);
        return jedis.clusterInfo();
    }

    @RequestMapping("/config/{nodeId}")
    public List<String> getConfig(@PathVariable String nodeId){
        Jedis jedis = jedisUtil.jedisByNodeId(nodeId);
        List list =jedis.configGet("*");

       // jedis.
        return list;
    }

}

