package cn.myframe.mvc.annotation;

import java.lang.annotation.*;

/**
 * @author ynz
 * @version 创建时间：2018/6/22
 * @email ynz@myframe.cn
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionBody {
    boolean required() default true;
}
