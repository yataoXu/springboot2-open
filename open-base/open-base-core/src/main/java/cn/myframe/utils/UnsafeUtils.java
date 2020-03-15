package cn.myframe.utils;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 反射获取Unsafe静态类
 *
 * @Author: ynz
 * @Date: 2019/1/14/014 13:16
 * @Version 1.0
 */
public class UnsafeUtils {

   static Unsafe unsafe = null;

    /**
     * 获取Unsafe
     */
    public synchronized static Unsafe getUnsafe(){
        if(unsafe == null){
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return unsafe;
        }
        return unsafe;
    }

    /**
     * 挂起线程,挂起调用该方法的线程
     * 第一个参数是是否是绝对时间，第二个参数是等待时间值。
     * 如果isAbsolute是true则会实现ms定时。如果isAbsolute是false则会实现ns定时
     */
    public static void park() {
        if(unsafe == null){
            getUnsafe();
        }
        //执行普通的挂起，isAbsolute是false,time是0。
        unsafe.park(false,0);
    }

    /**
     * 从挂起恢复
     * @param thread
     */
    public static void unpark(Thread thread){
        if(unsafe == null){
            getUnsafe();
        }
        unsafe.unpark(thread);
    }

    /**
     *cas样例
     */
    public static boolean compareAndSetState(Cas cas,int expect, int update){
        boolean b = false;
        try {
            long stateOffset = UnsafeUtils.getUnsafe().objectFieldOffset
                    (Cas.class.getDeclaredField("state"));
            //compareAndSwapObject
            return unsafe.compareAndSwapInt(cas,stateOffset,expect,update);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Data
    static final class Cas{
        int state = 0;
        static String name;
    }

    public static void main(String[] args) throws NoSuchFieldException {

    }
}
