package cn.myframe.listener;

import cn.myframe.controller.PrividerController;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * @Author: ynz
 * @Date: 2019/1/26/026 11:56
 * @Version 1.0
 */
@RocketMQTransactionListener(txProducerGroup="group1")
@Slf4j
public class TransactionListener  implements RocketMQLocalTransactionListener {

    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // local transaction process, return bollback, commit or unknown
        log.info("executeLocalTransaction:"+JSON.toJSONString(msg));
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        log.info("checkLocalTransaction:"+JSON.toJSONString(msg));
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
