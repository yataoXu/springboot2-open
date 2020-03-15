package cn.myframe.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.Serializable;
import java.util.List;

@Configuration
public class RedisConfig extends CachingConfigurerSupport{

	@Autowired
    RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }
    
    @Bean(name="factory")  
    public RedisConnectionFactory factory(JedisPoolConfig jedisPoolConfig){
        RedisClusterConfiguration redisClusterConfiguration =
                new RedisClusterConfiguration();
        List<String> nodeList = redisProperties.getCluster().getNodes();
        for(String node : nodeList){
            String[] host =  node.split(":");
            redisClusterConfiguration.addClusterNode(new RedisNode(host[0],Integer.parseInt(host[1])));
        }
        //设置密码
        if(StringUtils.isNotEmpty(redisProperties.getPassword())){
            redisClusterConfiguration.setPassword(redisProperties.getPassword());
        }
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！）
        jpcb.poolConfig(jedisPoolConfig);

        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();

        return new JedisConnectionFactory(redisClusterConfiguration,jedisClientConfiguration);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数
        jedisPoolConfig.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        //最小空闲连接数
        jedisPoolConfig.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
       //最大空闲连接数
        jedisPoolConfig.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        //当池内没有可用的连接时，最大等待时间
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().getSeconds()*1000L);

        //------其他属性根据需要自行添加-------------
        return jedisPoolConfig;
    }

}