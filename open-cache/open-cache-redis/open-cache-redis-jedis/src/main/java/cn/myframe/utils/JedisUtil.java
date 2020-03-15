package cn.myframe.utils;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;

/**
 * @Author: ynz
 * @Date: 2019/1/11/011 13:39
 * @Version 1.0
 */
public class JedisUtil {

    private  Jedis jedis;
    //
    public static Jedis getJedisClient(String ip, int port, String password) {
        Jedis jedis = new Jedis(ip, port);
       // String password = param.getRedisPassword();
        if (StringUtils.isNotBlank(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    public static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public boolean beSlave(String masterId){

        boolean result = true;
        try{
            jedis.clusterReplicate(masterId);
        }catch (Exception e){
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean beMaster() {
        boolean result = true;
        try {
            jedis.clusterFailover();
        } catch (Exception e) {

        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }


}
