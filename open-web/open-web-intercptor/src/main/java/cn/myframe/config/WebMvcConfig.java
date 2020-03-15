package cn.myframe.config;


import cn.myframe.intercptor.CustomHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * MVC拦截器配置
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2017年11月21日 下午12:27:49
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       // registry.addInterceptor(new URLInterceptor()).addPathPatterns("/**").excludePathPatterns("/login.html","/login");
        registry.addInterceptor(new CustomHandlerInterceptor()).addPathPatterns("/**");
    }


}

