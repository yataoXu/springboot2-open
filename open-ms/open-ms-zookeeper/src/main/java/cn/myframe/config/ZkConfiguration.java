package cn.myframe.config;


import cn.myframe.properties.ZookeeperProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ZookeeperProperties.class})
@ConditionalOnProperty(  //配置文件属性是否为true
        value = {"zookeeper.enabled"},
        matchIfMissing = false
)
public class ZkConfiguration {

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Value("${zookeeper.server}")
    private String zookeeperServer;
    @Value(("${zookeeper.sessionTimeoutMs}"))
    private int sessionTimeoutMs;
    @Value("${zookeeper.connectionTimeoutMs}")
    private int connectionTimeoutMs;
    @Value("${zookeeper.maxRetries}")
    private int maxRetries;
    @Value("${zookeeper.baseSleepTimeMs}")
    private int baseSleepTimeMs;
    @Value("${zookeeper.namespace}")
    private String namespace;

    @Bean(initMethod = "init", destroyMethod = "stop")
    public ZkClient zkClient() {
        ZkClient zkClient = new ZkClient(zookeeperProperties);
        return zkClient;
    }

    /**
     * 用于分布于测试，正式使用请删除
     * @return
     */
    @Bean(initMethod = "init", destroyMethod = "stop")
    public ZkClient zkClientTest() {
        ZkClient zkClient = new ZkClient(zookeeperProperties);
        return zkClient;
    }

}

