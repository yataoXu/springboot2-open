package cn.myframe.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * canal 监听器注解，继承 @Component
 *
 * @Author: ynz
 * @Date: 2019/4/17/017 8:13
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface CanalEventListener {
    @AliasFor(annotation = Component.class)
    String value() default "";
}
