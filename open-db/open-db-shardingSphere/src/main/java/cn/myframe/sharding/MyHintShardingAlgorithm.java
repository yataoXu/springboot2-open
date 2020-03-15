package cn.myframe.sharding;

import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author: ynz
 * @Date: 2019/6/5/005 13:46
 * @Version 1.0
 */
@Component
public class MyHintShardingAlgorithm implements HintShardingAlgorithm {
    @Override
    public Collection<String> doSharding(Collection<String> collection, ShardingValue shardingValue) {
        return null;
    }
}
