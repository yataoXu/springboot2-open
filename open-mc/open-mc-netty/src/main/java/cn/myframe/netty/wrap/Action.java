package cn.myframe.netty.wrap;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author ynz
 * @version 创建时间：2018/6/21
 * @email ynz@myframe.cn
 */
@Data
public class Action {
    String httpMethod;
    Class<?> controllerClass;
    Method method;
    String[] argNames;
    Object bean;
    String responseType;
   // AbstractActionIntercptor interceptor;

    public Action() {
    }

    /**
     * @param httpMethod
     * @param controllerClass
     * @param method
     * @param argNames
     */
    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
    }
    
    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames,Object bean) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
        this.bean = bean;
    }

    public Action(String httpMethod, Class<?> controllerClass, Method method, String[] argNames,Object bean, String responseType) {
        super();
        this.httpMethod = httpMethod;
        this.controllerClass = controllerClass;
        this.method = method;
        this.argNames = argNames;
        this.bean = bean;
        this.responseType = responseType;
    }
}
