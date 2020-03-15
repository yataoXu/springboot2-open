package cn.myframe.config;

import cn.myframe.service.RemoteUserService;
import cn.myframe.service.eventnotity.IDemoService;
import cn.myframe.service.eventnotity.NotifyImpl;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.spring.boot.DubboProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 9:59
 * @Version 1.0
 */
@Configuration
public class DubboConfig {

    @Autowired
    NotifyImpl notify;

    @Autowired
    DubboProperties dubboProperties;

    @Bean
    public IDemoService demoService(){
        ReferenceConfig<IDemoService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(IDemoService.class);
        referenceConfig.setTimeout(5000);
        referenceConfig.setVersion("2.7.0");
        referenceConfig.setRetries(3);
        referenceConfig.setCheck(false);
        referenceConfig.setRegistry(dubboProperties.getRegistry());
        referenceConfig.setApplication(dubboProperties.getApplication());

        //配置每一个method的信息
        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("get");
        methodConfig.setTimeout(1000);
        methodConfig.setAsync(true);
        methodConfig.setOnreturn(notify);
        methodConfig.setOnreturnMethod("onreturn");
        //将method的设置关联到service配置中
        List<MethodConfig> methods = new ArrayList<>();
        methods.add(methodConfig);
        referenceConfig.setMethods(methods);
        return referenceConfig.get();
    }

    @Bean
    public RemoteUserService remoteUserService(){
        ReferenceConfig<RemoteUserService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(RemoteUserService.class);
        referenceConfig.setTimeout(5000);
        referenceConfig.setVersion("2.6.0");
        referenceConfig.setRetries(3);
        referenceConfig.setCheck(false);
        referenceConfig.setRegistry(dubboProperties.getRegistry());
        referenceConfig.setApplication(dubboProperties.getApplication());
        return referenceConfig.get();
    }

}
