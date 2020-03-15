package cn.myframe.config;

import cn.myframe.client.SimpleCanalClient;
import cn.myframe.properties.CanalProperties;
import cn.myframe.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


public class CanalClientConfiguration {
    /**
     * 记录日志
     */
    private final static Logger logger = LoggerFactory.getLogger(CanalClientConfiguration.class);

    /**
     * canal 配置
     */
    @Autowired
    private CanalProperties canalProperties;
    
    /**
     * 返回 bean 工具类
     *
     * @param
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BeanUtil beanUtil() {
        return new BeanUtil();
    }
    
    /**
     * 返回 canal 的客户端
     *
     */
    @Bean
    private SimpleCanalClient canalClient() {
        logger.info("正在尝试连接 canal 客户端....");
        //连接 canal 客户端
//        CanalClient canalClient = new SimpleCanalClient(canalConfig, MessageTransponders.defaultMessageTransponder());
        SimpleCanalClient canalClient = new SimpleCanalClient(canalProperties);
        logger.info("正在尝试开启 canal 客户端....");
        //开启 canal 客户端
        canalClient.start();
        logger.info("启动 canal 客户端成功....");
        //返回结果
        return canalClient;
    }
}
