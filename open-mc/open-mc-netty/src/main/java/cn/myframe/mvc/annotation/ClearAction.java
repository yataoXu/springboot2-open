package cn.myframe.mvc.annotation;

import java.lang.annotation.*;

/**
 * 
 * 忽略请求
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 上午9:03:03
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClearAction {
}
