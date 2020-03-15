package cn.myframe.server;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 
 * 消息的消费者
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月14日 上午10:51:36
 */
@Component("rabbitReceiver")
@Slf4j
public class Consumer {

	 /**
     * FANOUT广播队列监听一.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"FANOUT_QUEUE_A"},containerFactory = "rabbitListenerContainerFactory")
    @Async
    public void on(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        //  log.info("FANOUT_QUEUE_A "+new String(message.getBody()));
    }

    /**
     * FANOUT广播队列监听二.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception   这里异常需要处理
     */
    @RabbitListener(queues = {"FANOUT_QUEUE_B"})
    @Async
    public void t(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        //log.info("FANOUT_QUEUE_B "+new String(message.getBody()));
    }

    /**
     * DIRECT模式.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"DIRECT_QUEUE"})
    public void message(Message message, Channel channel) throws IOException {
    	log.info("收到队列[DIRECT_QUEUE]的消息{}",new String (message.getBody()));
    	//消息的标识,false只确认当前一个消息收到，true确认所有consumer获得的消息
    	//拒绝消息      channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        //channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        
    }
    
    /**
     * DIRECT模式.
     *
     * @param message the message
     * @param channel the channel
     * @throws IOException the io exception  这里异常需要处理
     */
    @RabbitListener(queues = {"DIRECT_QUEUE"})
    public void message2(Message message, Channel channel) throws IOException {
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        log.debug("DIRECT2 "+new String (message.getBody()));
    }
}
