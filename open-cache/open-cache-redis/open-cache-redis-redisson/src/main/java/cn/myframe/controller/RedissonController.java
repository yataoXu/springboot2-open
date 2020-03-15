package cn.myframe.controller;

import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/4/29/029 17:19
 * @Version 1.0
 */
@RestController
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @RequestMapping("/add/{name}")
    public String setValue(@PathVariable String name){
        // 设置字符串
        RBucket<String> keyObj = redissonClient.getBucket("k1");
        keyObj.set(name);

        RList<String> rList = redissonClient.getList("l1");

        RTopic topic = redissonClient.getTopic("anyTopic");
        long clientsReceivedMessage = topic.publish(name);

        RBloomFilter<Integer> bloomFilter = redissonClient.getBloomFilter("sample");
        // 初始化布隆过滤器，预计统计元素数量为55000000，期望误差率为0.03
        bloomFilter.tryInit(55000000L, 0.003);

        RHyperLogLog<Integer> log = redissonClient.getHyperLogLog("log");



        int count = 0;
        /*for (int i = 0; i < 100000; i++) {
            bloomFilter.add(i);
        }*/
        for (int i = 100000; i < 100500; i++) {
            boolean b = bloomFilter.contains(i);
            if(b){
                count ++;
            }

        }
        System.out.println(count);


        //redissonClient.getScript().evalSha()
        return name;


    }

    @RequestMapping("/map/{name}")
    public String testMap(@PathVariable String name){
        RMap<String, String> map = redissonClient.getMap("anyMap");
        map.put("test","test");

        RMapCache cache =  redissonClient.getMapCache("RMapCache");
        for (int i = 0; i < 10; i++) {
            cache.put("key"+i, "key"+i, i, TimeUnit.MINUTES);
        }

        return name;
    }

    @PostConstruct
    public void topic(){
        RTopic topic = redissonClient.getTopic("anyTopic");
        topic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String s) {
                System.out.println(s);
            }
        });
    }
}
