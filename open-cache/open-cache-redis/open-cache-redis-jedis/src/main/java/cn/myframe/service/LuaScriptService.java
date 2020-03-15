package cn.myframe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.util.JedisClusterCRC16;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ynz
 * @Date: 2019/2/20/020 11:00
 * @Version 1.0
 */
@Service
public class LuaScriptService {

    @Autowired
    private RedisTemplate<String,Serializable> redisTemplate;

    private DefaultRedisScript<List> getRedisScript;

    static  Map<String,AtomicInteger> count = new HashMap<>();

    String lua = "";
    String scriptLoad  = null;
    static{
        count.put("success",new AtomicInteger());
        count.put("fail",new AtomicInteger());
    }

   @PostConstruct
    public void init(){

       try {
            lua =  new ResourceScriptSource(new ClassPathResource("lua/ratelimit.lua")).getScriptAsString();
       } catch (IOException e) {
           e.printStackTrace();
       }
       new Thread(()->{
            while (true){
                for(Map.Entry<String,AtomicInteger> entry:count.entrySet()){
                    System.out.println(entry.getKey()+":"+entry.getValue().get());
                    entry.getValue().set(0);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        getRedisScript = new DefaultRedisScript<List>();
        getRedisScript.setResultType(List.class);
        getRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/ratelimit2.lua")));
       System.out.println(getRedisScript.getSha1());
    }

    public void redisAddScriptExec(){
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList();
        keyList.add("key1{mykey}");
     //   keyList.add("count2");
     //   int slot = JedisClusterCRC16.getSlot("count1");
     //   int slot2 = JedisClusterCRC16.getSlot("count2");
     //   keyList.add("rate.limiting:127.0.0.1");

        /**
         * 用Map设置Lua的ARGV[1]
         */
        List<String> argvList = new ArrayList<>();
        /*Map<String,Object> argvMap = new HashMap<String,Object>();
        argvMap.put("expire",1000);
        argvMap.put("times",10);*/
        argvList.add("2000");//times次数
        argvList.add("1");//expire超时时间
        argvList.add(String.valueOf(new Random().nextInt(100)));//expire超时时间
        argvList.add(String.valueOf(new Random().nextInt(10)));//ip
        argvList.add("key2{mykey}");//ip


        /**
         * 调用脚本并执行
         */
     //   List result = redisTemplate.execute(getRedisScript,keyList, argvList);
        try {

          //  String LUA = "redis.call('SET', KEYS[1], ARGV[1]); return ARGV[1]";
            Long result = redisTemplate.execute(new RedisCallback<Long>() {
                public Long doInRedis(RedisConnection connection) throws DataAccessException {
                    Object nativeConnection = connection.getNativeConnection();
                    // 集群模式和单点模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                    // 集群
                    if (nativeConnection instanceof JedisCluster) {
                        if(scriptLoad == null){
                             scriptLoad  = ((JedisCluster) nativeConnection).scriptLoad(lua,"luaTest");
                        }

                       // System.out.println(scriptLoad);
                       // System.out.println( ((JedisCluster) nativeConnection).scriptExists(scriptLoad, "luaTest"));
                        return (Long) ((JedisCluster) nativeConnection).evalsha(scriptLoad, keyList, argvList);
                    }// 单点
                    else if (nativeConnection instanceof Jedis) {
                        return (Long) ((Jedis) nativeConnection).eval(getRedisScript.getSha1(), keyList, argvList);
                    }
                    return null;
                }
            });
            if(result== 1){
                count.get("success").getAndIncrement();
            }else{
                count.get("fail").getAndIncrement();
            }
           // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }




      //  System.out.println(result);

    }

}
