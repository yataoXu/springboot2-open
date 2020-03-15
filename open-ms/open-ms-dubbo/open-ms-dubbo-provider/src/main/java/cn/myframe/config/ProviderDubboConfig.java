package cn.myframe.config;

import cn.myframe.service.RemoteUserService;
import cn.myframe.service.callback.CallbackService;
import com.alibaba.dubbo.config.ArgumentConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.spring.boot.DubboProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 13:03
 * @Version 1.0
 */
/*@Component*/
public class ProviderDubboConfig {

    @Autowired(required = false)
    RemoteUserService userService;

    @Autowired(required = false)
    CallbackService callbackService;

    @Autowired(required = false)
    DubboProperties dubboProperties;

    @PostConstruct
    public void userServiceConfig(){
        ServiceConfig<RemoteUserService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(RemoteUserService.class);
        serviceConfig.setRef(userService);
        serviceConfig.setVersion("2.6.0");
        serviceConfig.setApplication(dubboProperties.getApplication());
        serviceConfig.setRegistry(dubboProperties.getRegistry());
        serviceConfig.setProtocol(dubboProperties.getProtocol());
       // serviceConfig.setProtocols(dubboProperties.getProtocols());

        //配置每一个method的信息
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("sayHello");
        methodConfig.setTimeout(1000);

        //将method的设置关联到service配置中
        List<MethodConfig> methods = new ArrayList<>();
        methods.add(methodConfig);
        serviceConfig.setMethods(methods);
        serviceConfig.export();
       // return serviceConfig;
    }

    @PostConstruct
    public void callBackrServiceConfig(){
        ServiceConfig<CallbackService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(CallbackService.class);
        serviceConfig.setRef(callbackService);
        serviceConfig.setVersion("2.6.0");
        serviceConfig.setApplication(dubboProperties.getApplication());
        serviceConfig.setRegistry(dubboProperties.getRegistry());
        serviceConfig.setProtocol(dubboProperties.getProtocol());
        serviceConfig.setCallbacks(100); //设置回调参数个数

        //配置每一个method的信息
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("addListener");

        ArgumentConfig argumentConfig = new ArgumentConfig();
        argumentConfig.setIndex(1);
        argumentConfig.setCallback(true);
        methodConfig.setArguments(Arrays.asList(argumentConfig));

        //将method的设置关联到service配置中
        List<MethodConfig> methods = new ArrayList<>();
        methods.add(methodConfig);
        serviceConfig.setMethods(methods);
        serviceConfig.export();
    }

}
