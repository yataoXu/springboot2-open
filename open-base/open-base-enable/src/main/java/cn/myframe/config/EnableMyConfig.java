package cn.myframe.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: ynz
 * @Date: 2019/4/16/016 17:25
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MyConfiguration.class})
public @interface EnableMyConfig {
}
