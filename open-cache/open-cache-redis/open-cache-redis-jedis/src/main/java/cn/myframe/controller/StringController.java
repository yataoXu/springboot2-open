package cn.myframe.controller;

import cn.myframe.annotation.CalTime;
import cn.myframe.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ynz
 * https://blog.csdn.net/qq_25135655/article/details/80357137
 */
@RestController
public class StringController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    static AtomicInteger atomicInteger = new AtomicInteger();

    @Autowired
    JedisUtils jedisUtil;


    @CalTime
    @RequestMapping("/string/addMap/{num}")
    public String add(@PathVariable int num) throws InterruptedException {
        Map<String,String> map = new HashMap<>();
        CountDownLatch countDownLatch = new CountDownLatch(num);
        for (int j = 0; j < num; j++) {
            new Thread(()->{
                for (int i = 0; i < 1000; i++) {
                    int index = atomicInteger.getAndIncrement();
                     //map.put("map:"+index,String.valueOf(index));
                    redisTemplate.opsForValue().set("map3:"+index,String.valueOf(index));
                }
                countDownLatch.countDown();
            }).start();

        }
        countDownLatch.await();
        return "add";
    }

    @CalTime
    @RequestMapping("/string/pAdd/{num}")
    public String pipelineAdd(@PathVariable int num){
        // 获取key编码方式
        final StringRedisSerializer stringRedisSerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        //获取值编码方式
        final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
        Map<String,List<String>> map = new HashMap<>();
        for (int i = 0; i < num; i++) {
            String key = "map4:"+i;
            String nodeId = jedisUtil.getNodeId(key);
            List<String> list = map.get(nodeId);
            if(list == null){
                list = new ArrayList<String>();
                map.put(nodeId,list);
            }
            list.add(key);
        }
        for(Map.Entry<String,List<String>> e: map.entrySet()){
            Jedis jedis = jedisUtil.jedisByNodeId(e.getKey());
            List<String> list = e.getValue();
            Pipeline p = jedis.pipelined();
            for(String key : list){
                p.set(stringRedisSerializer.serialize(key),valueSerializer.serialize(key));
            }
            p.sync();
        }
        return "batchAdd";
    }



    @RequestMapping("/string/get/{key}")
    public String get(@PathVariable  String key){
      return  redisTemplate.opsForValue().get(key);
    }

    @RequestMapping("/string/incr/{threadNum}")
    public String incr(@PathVariable int threadNum) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for(int i=0;i<threadNum;i++){
            new Thread(()->{
                for(int j=0;j<1000;j++){
                    redisTemplate.opsForValue().increment("doubleValue",1);
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        String value = String.valueOf(redisTemplate.opsForValue().get("doubleValue"));
     //   redisTemplate.opsForValue().re
        return "value:"+value;
    }



}
