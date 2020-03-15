package cn.myframe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
//https://blog.csdn.net/u011126891/article/details/54376179
//https://www.cnblogs.com/daiwei1981/p/9403816.html
@Configuration
@Slf4j
public class PrividerRabbitmqConfig {
	 
	    @Resource
	    private RabbitTemplate rabbitTemplate;



	    /**
	     * 定制化amqp模版      可根据需要定制多个
	     * 
	     * 
	     * 此处为模版类定义 Jackson消息转换器
	     * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
	     * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
	     *
	     * @return the amqp template
	     */
	    @Bean
	    public AmqpTemplate amqpTemplate() {
           //使用jackson 消息转换器
	        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
	        rabbitTemplate.setEncoding("UTF-8");
           //开启returncallback     yml 需要 配置    publisher-returns: true
	        rabbitTemplate.setMandatory(true);
	        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
	            log.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", replyCode, replyText, exchange, routingKey);
	        });
	        //消息确认模式  yml 需要配置   publisher-returns: true
	        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
	            if (ack) {
	                log.info("消息发送到exchange成功,id: {}", correlationData.getId());
	            } else {
	                log.info("消息发送到exchange失败,原因: {}", cause);
	            }
	        });


	        return rabbitTemplate;
	    }

	    /* ----------------------------------------------------------------------------Direct exchange test--------------------------------------------------------------------------- */

	    /**
	     * 声明Direct交换机 支持持久化.
	     *
	     * @return the exchange
	     */
	    @Bean("directExchange")
	    public Exchange directExchange() {
	        return ExchangeBuilder.directExchange("DIRECT_EXCHANGE").durable(true).build();
	    }

	    /**
	     * 声明一个队列 支持持久化.
	     *
	     * @return the queue
	     */
	    @Bean("directQueue")
	    public Queue directQueue() {
	        return QueueBuilder.durable("DIRECT_QUEUE").build();
	    }

	    /**
	     * 通过绑定键 将指定队列绑定到一个指定的交换机 .
	     *
	     * @param queue    the queue
	     * @param exchange the exchange
	     * @return the binding
	     */
	    @Bean
	    public Binding directBinding(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
	        return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs();
	    }

	    /* ----------------------------------------------------------------------------Fanout exchange test--------------------------------------------------------------------------- */

	    /**
	     * 声明 fanout 交换机.
	     *
	     * @return the exchange
	     */
	    @Bean("fanoutExchange")
	    public FanoutExchange fanoutExchange() {
	        return (FanoutExchange) ExchangeBuilder.fanoutExchange("FANOUT_EXCHANGE").durable(true).build();
	    }

	    /**
	     * Fanout queue A.
	     *
	     * @return the queue
	     */
	    @Bean("fanoutQueueA")
	    public Queue fanoutQueueA() {
	        return QueueBuilder.durable("FANOUT_QUEUE_A").build();
	    }

	    /**
	     * Fanout queue B .
	     *
	     * @return the queue
	     */
	    @Bean("fanoutQueueB")
	    public Queue fanoutQueueB() {
	        return QueueBuilder.durable("FANOUT_QUEUE_B").build();
	    }

	    /**
	     * 绑定队列A 到Fanout 交换机.
	     *
	     * @param queue          the queue
	     * @param fanoutExchange the fanout exchange
	     * @return the binding
	     */
	    @Bean
	    public Binding bindingA(@Qualifier("fanoutQueueA") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
	        return BindingBuilder.bind(queue).to(fanoutExchange);
	    }

	    /**
	     * 绑定队列B 到Fanout 交换机.
	     *
	     * @param queue          the queue
	     * @param fanoutExchange the fanout exchange
	     * @return the binding
	     */
	    @Bean
	    public Binding bindingB(@Qualifier("fanoutQueueB") Queue queue, @Qualifier("fanoutExchange") FanoutExchange fanoutExchange) {
	        return BindingBuilder.bind(queue).to(fanoutExchange);
	    }

		/**
		 * 超时队列
		 * 消息被拒绝（basic.reject/ basic.nack）并且requeue=false
		 * 消息TTL过期（参考：RabbitMQ之TTL（Time-To-Live 过期时间））
		 * 队列达到最大长度
		 * @return
		 */
		@Bean("deadLetterQueue")
		public Queue deadLetterQueue() {
			Map<String, Object> arguments = new HashMap<>();
			arguments.put("x-dead-letter-exchange", "DIRECT_EXCHANGE");
			arguments.put("x-dead-letter-routing-key", "DIRECT_ROUTING_KEY");
			Queue queue = new Queue("DEAD_LETTER_QUEUE",true,false,false,arguments);
			return queue;
		}

		@Bean
		public Binding  deadLetterBinding(@Qualifier("deadLetterQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
			return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY2").noargs();
		}

		/**
		 * 优先级队列
		 * @return
		 */
	/*	@Bean("priorityQueue")*/
		public Queue priorityQueue(){
			Map<String, Object> arguments = new HashMap<>();
			arguments.put("x-max-priority",10); //队列的属性参数 有10个优先级别
			Queue queue = new Queue("DEAD_LETTER_QUEUE",true,false,false,arguments);
			return queue;
		}

	/*	@Bean
		public Binding priorityBinding(@Qualifier("priorityQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
			return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs();
		}*/
}
