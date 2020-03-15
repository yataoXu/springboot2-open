package cn.myframe.utils;

import cn.myframe.entity.ClusterNodeObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @Author: ynz
 * 根据key的slot查找节点,返回jedis
 */
@Component
public class JedisUtils {

    @Autowired
    RedisConnectionFactory factory;

    List<ClusterNodeObject> nodelist = new ArrayList<>();

    Map<String,ClusterNodeObject> nodeMap = new HashMap<String,ClusterNodeObject>();

    @PostConstruct
    public void init()  {
        RedisConnection redisConnection = factory.getConnection();
        JedisClusterConnection jedisClusterConnection = (JedisClusterConnection) redisConnection;
        // 获取到原始到JedisCluster连接
        JedisCluster jedisCluster = jedisClusterConnection.getNativeConnection();
        Set<String> set = jedisCluster.getClusterNodes().keySet();
        for(String key :set ){
            JedisPool jedisPool = jedisCluster.getClusterNodes().get(key);
            Jedis jedis = jedisPool.getResource();

            String clusterNodesCommand = jedis.clusterNodes();
            String[] allNodes = clusterNodesCommand.split("\n");
            for (String allNode : allNodes) {
                String[] splits = allNode.split(" ");

                String hostAndPort = splits[1];
                if(splits[2].contains("myself")){
                    ClusterNodeObject clusterNodeObject =
                            new ClusterNodeObject(splits[0], splits[1], splits[2].contains("master"), splits[3],
                                    splits[7].equalsIgnoreCase("connected"), splits.length == 9 ? splits[8] : null,jedis);
                    nodelist.add(clusterNodeObject);
                }

            }
        }

    }

    /**
     * 返回jedis对象
     * @param key
     * @return
     */
    public Jedis getJedis(String key){
        int slot = JedisClusterCRC16.getSlot(key);
        for(ClusterNodeObject node :nodelist){
            if (node != null && node.getSolts()!=null) {
                String[] slots = node.getSolts().split("-");
                if(Integer.parseInt(slots[0])<=slot && slot <=Integer.parseInt(slots[1]) ){
                    return node.getJedis();
                }
            }
        }
        return null;
    }

    public String getNodeId(String key) {
        int slot = JedisClusterCRC16.getSlot(key);
        for (ClusterNodeObject node : nodelist) {
            if (node != null && node.getSolts() != null) {
                String[] slots = node.getSolts().split("-");
                if (Integer.parseInt(slots[0]) <= slot && slot <= Integer.parseInt(slots[1])) {
                    return node.getNodeId();
                }
            }
        }
        return null;
    }

    public Jedis jedisByNodeId(String nodeId){
        for (ClusterNodeObject node : nodelist) {
            if(node.getNodeId().equals(nodeId)){
                return node.getJedis();
            }
        }
        return null;
    }

    public Pipeline getPipeline(String key)  {
        //RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        RedisConnection redisConnection = factory.getConnection();
        JedisClusterConnection jedisClusterConnection = (JedisClusterConnection) redisConnection;
        // 获取到原始到JedisCluster连接
        JedisCluster jedisCluster = jedisClusterConnection.getNativeConnection();
        // 通过key获取到具体的Jedis实例
        // 计算hash slot，根据特定的slot可以获取到特定的Jedis实例
        int slot = JedisClusterCRC16.getSlot(key);

        Field field = ReflectionUtils.findField(BinaryJedisCluster.class, null, JedisClusterConnectionHandler.class);
        field.setAccessible(true);
        JedisSlotBasedConnectionHandler jedisClusterConnectionHandler = null;
        try {
            jedisClusterConnectionHandler = (JedisSlotBasedConnectionHandler) field.get(jedisCluster);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Jedis jedis = jedisClusterConnectionHandler.getConnectionFromSlot(slot);
        // 接下来就是pipeline操作了
        Pipeline pipeline = jedis.pipelined();
        return pipeline;

    }

}
