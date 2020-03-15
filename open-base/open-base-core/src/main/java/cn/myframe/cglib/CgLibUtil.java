package cn.myframe.cglib;

import net.sf.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * <p/>
 *
 * @author cs12110 created at: 2019/1/2 16:51
 * <p>
 * since: 1.0.0
 */
public class CgLibUtil implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public <T> T getProxy(Class<T> target) {
        enhancer.setSuperclass(target);
        enhancer.setCallback(this);

//        CallbackFilter filter = new CallbackFilter() {
//            @Override
//            public int accept(Method method) {
//                if ("toString".equals(method.getName())) {
//                    return 1;
//                }
//                return 0;
//            }
//        };
        //enhancer.setCallbackFilter(filter);
        Object proxy = enhancer.create();
        return (T) proxy;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        doBefore(method.getName());
        Object result = methodProxy.invokeSuper(o, objects);
        doAfter(method.getName());
        return result;
    }


    private void doBefore(String methodName) {
        System.out.println("do before: " + methodName);
    }

    private void doAfter(String methodName) {
        System.out.println("do after: " + methodName);
    }
}
