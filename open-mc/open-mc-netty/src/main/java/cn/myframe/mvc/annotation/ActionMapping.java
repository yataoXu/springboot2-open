
package cn.myframe.mvc.annotation;

import java.lang.annotation.*;

/**
 * 
 * 类似RequestMapping
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:22:36
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActionMapping {
	
	String actionKey() default "";

	String httpMethod() default "";
}
