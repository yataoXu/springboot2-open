package cn.myframe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import redis.clients.jedis.Jedis;

/**
 * @Author: ynz
 * @Date: 2019/1/23/023 15:21
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class ClusterNodeObject {

    private String nodeId;

    private String hostAndPort;

    private boolean master;

    private String masterNodeId;

    private boolean connect;

    private String solts;

    private Jedis jedis;


}
