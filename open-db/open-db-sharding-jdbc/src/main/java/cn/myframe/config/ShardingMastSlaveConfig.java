package cn.myframe.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.shardingjdbc.core.api.config.MasterSlaveRuleConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "sharding.jdbc")
public class ShardingMastSlaveConfig {

    private Map<String, DruidDataSource> dataSources = new HashMap<>();

    private MasterSlaveRuleConfiguration masterSlaveRule;
}
