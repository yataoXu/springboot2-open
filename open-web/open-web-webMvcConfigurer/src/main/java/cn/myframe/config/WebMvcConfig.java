package cn.myframe.config;

import cn.myframe.entity.PreUser;
import cn.myframe.interceptor.CustomHandlerInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //可以通过http://127.0.0.1:7091/web/home.html访问resources/web/home.html页面
        registry.addResourceHandler("/web/**").addResourceLocations("classpath:/web/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("web/login.html");
    }


    /**
     * 跨域CORS配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //添加映射路径
        registry.addMapping("/**")
                //放行哪些原始域
                .allowedOrigins("*")
                //是否发送Cookie信息
                .allowCredentials(true)
                //放行哪些原始域(请求方式)
                .allowedMethods("GET","POST", "PUT", "DELETE")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*")
                //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                .exposedHeaders("Header1", "Header2");

    }

    /**
     * 消息内容转换配置
     * 配置fastJson返回json转换
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建fastJson消息转换器
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter(){
            protected void writeInternal(Object obj, HttpOutputMessage outputMessage){
                try {
                    if(obj instanceof PreUser){
                        Map<String,Object> map = new HashMap<>();
                        map.put("preUser",obj);
                        map.put("result","success");
                        super.writeInternal( map, outputMessage);
                    }else{
                        super.writeInternal( obj, outputMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
      //  FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        //创建配置类
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setDateFormat("YYYY-MM-dd");

        //修改配置返回内容的过滤
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.PrettyFormat
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //将fastjson添加到视图消息转换器列表内
        converters.add(fastConverter);
    }


    @Configuration
    @ConditionalOnClass({FastJsonHttpMessageConverter.class}) //1
    @ConditionalOnProperty(//2
            name = {"spring.http.converters.preferred-json-mapper"},
            havingValue = "fastjson",
            matchIfMissing = true
    )
    protected static class FastJson2HttpMessageConverterConfiguration {
        protected FastJson2HttpMessageConverterConfiguration() {
        }
        @Bean
        @ConditionalOnMissingBean({FastJsonHttpMessageConverter.class})//3
        public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
            //创建fastJson消息转换器
            FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter(){
                protected void writeInternal(Object obj, HttpOutputMessage outputMessage){
                    try {
                        if(obj instanceof PreUser){
                            Map<String,Object> map = new HashMap<>();
                            map.put("result","success");
                            map.put("preUser",obj);
                            super.writeInternal( map, outputMessage);
                        }else{
                            super.writeInternal( obj, outputMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            //  FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
            List<MediaType> supportedMediaTypes = new ArrayList<>();
            supportedMediaTypes.add(MediaType.APPLICATION_JSON);
            supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
            fastConverter.setSupportedMediaTypes(supportedMediaTypes);

            //创建配置类
            FastJsonConfig fastJsonConfig = new FastJsonConfig();
            fastJsonConfig.setCharset(Charset.forName("UTF-8"));
            fastJsonConfig.setDateFormat("YYYY-MM-dd");

            //修改配置返回内容的过滤
            fastJsonConfig.setSerializerFeatures(
                    SerializerFeature.DisableCircularReferenceDetect,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteNullStringAsEmpty,
                    SerializerFeature.PrettyFormat
            );
            fastConverter.setFastJsonConfig(fastJsonConfig);
            //将fastjson添加到视图消息转换器列表内
           // converters.add(fastConverter);
            return fastConverter;
        }
    }
}

