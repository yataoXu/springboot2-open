package cn.myframe.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * @Author: ynz
 * @Date: 2018/12/25/025 13:38
 * @Version 1.0
 */
/*@Configuration*/
public class multipartConfig {
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.dir") + "/data/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }
        DataSize maxFileSize = DataSize.of(1,DataUnit.MEGABYTES);//1M
        DataSize maxRequestSize = DataSize.of(1,DataUnit.MEGABYTES);//1M
        factory.setLocation(location);
        factory.setMaxFileSize(maxFileSize);
        factory.setMaxRequestSize(maxRequestSize);
        return factory.createMultipartConfig();
    }
}
