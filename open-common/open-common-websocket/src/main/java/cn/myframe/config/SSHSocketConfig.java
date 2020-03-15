package cn.myframe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 15:04
 */
@Configuration
// 此注解表示使用STOMP协议来传输基于消息代理的消息，此时可以在@Controller类中使用@MessageMapping
@EnableWebSocketMessageBroker
public class SSHSocketConfig  implements WebSocketMessageBrokerConfigurer {

    /**
     * setAllowedOrigins方法用来设置来自那些域名的请求可访问，默认为localhost
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*");
         //SockJS客户端访问
        /*registry.addEndpoint("/my-websocket").withSockJS();*/
    }

    /**
     * 配置消息代理
     * 启动Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
     */
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        /**
         * 配置消息代理
         * 启动简单Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
         */
        registry.enableSimpleBroker("/topic");
        //只接收这前缀发送过来的消息
        registry.setApplicationDestinationPrefixes("/send");//应用请求前缀
    }
}
