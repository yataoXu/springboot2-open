package cn.myframe.entity;

import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
@PropertySource("classpath:application.yml")
public class ValueModel {

    @Value("I LOVE YOU!") //注入普通字符串
    private String normal;

    @Value("#{systemProperties['os.name']}") //注入操作系统属性
    private String osName;

    @Value("#{T(java.lang.Math).random() * 100.0}") //注入表达式结果
    private String randomNumber;

    @Value("#{user.address}") //注入其他Bean属性
    private String address;

    @Value("classpath:/test.txt") //注入文件资源
    private Resource testFile;

    @Value("http://www.baidu.com") //注入网址资源
    private Resource testUrl;

    @Value("${myframe.user.name}") // 注入配置文件
    private String userName;

    @Autowired // 注入配置文件
    private Environment environment;

    @Bean // 注入配置文件
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource(){
        try {
            System.out.println(normal);
            System.out.println(osName);
            System.out.println(randomNumber);
            System.out.println(address);
            System.out.println(IOUtils.toString(testFile.getInputStream()));
            System.out.println(IOUtils.toString(testUrl.getInputStream()));
            System.out.println(userName);
            System.out.println(environment.getProperty("myframe.user.name"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
