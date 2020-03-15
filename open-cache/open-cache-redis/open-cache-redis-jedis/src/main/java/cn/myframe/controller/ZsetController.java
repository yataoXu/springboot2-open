package cn.myframe.controller;

import cn.myframe.entity.ClusterNodeObject;
import cn.myframe.utils.JedisUtils;
import cn.myframe.utils.NameBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @Author: ynz
 * https://blog.csdn.net/XinhuaShuDiao/article/details/84906538
 */
@RestController
public class ZsetController {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    JedisUtils jedisUtils;

    private static final String KEY = "GAME";

    @RequestMapping("/zset/add/{count}")
    public String add(@PathVariable int count){
        long startTime = System.currentTimeMillis();
       /* for(int i = 0;i<count; i++){
            Double d = new Random().nextDouble();
            redisTemplate.opsForZSet().add(KEY,NameBuildUtils.buildName(),d);
        }
*/
        // 获取key编码方式
        final StringRedisSerializer stringRedisSerializer = (StringRedisSerializer) redisTemplate.getKeySerializer();
        //获取值编码方式
        final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) redisTemplate.getValueSerializer();
        //获取key对应的byte[]
        final byte[] rawKey = stringRedisSerializer.serialize(KEY);

       // Pipeline p = getPipeline();
        Pipeline p = jedisUtils.getJedis(KEY).pipelined();
        for(int i = 0;i<count; i++){
            String value = NameBuildUtils.buildName();
            Double d = new Random().nextDouble();
            p.zadd(rawKey,d,valueSerializer.serialize(value));
        }
        List<Object> list = p.syncAndReturnAll();
        System.out.println(list.size());
        return "add:"+(System.currentTimeMillis()-startTime);
    }


    /**
     *
     * @return
     */
    @RequestMapping("/zset/rangeSocre")
    public  Set<String>  rangeByScore(){
        Set<String> setSocre = redisTemplate.opsForZSet().reverseRangeByScore(KEY,0.9D,1D,0,10);
        return setSocre;
    }




}
