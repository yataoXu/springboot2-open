package cn.myframe.jvm.classloader;

/**
 * JAVA类装载方式，有两种:
 * 隐式装载， 程序在运行过程中当碰到通过new 等方式生成对象时，隐式调用类装载器加载对应的类到jvm中
 * 显式装载， 通过class.forname()等方法，显式加载需要的类
 *
 * 类加载的动态性体现:先把保证程序运行的基础类一次性加载到jvm中，其它类等到jvm用到的时候再加载，这样的好处是节省了内存的开销
 *
 * AppClassLoader负责装载classpath路径下的类包
 */
public class ClassLoaderExample extends ClassLoader{

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = ClassLoaderExample.class.getClassLoader();
       // classLoader.loadClass("");
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
      //  Class.forName("cn.myframe.ClassLoaderExample");
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("hello"));
        System.out.println(System.getProperty("hi"));
        System.out.println(System.getProperty("java.class.path"));

        //通过线程上下文类加载器加载
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        //获取系统的类加载器
        ClassLoader system = getSystemClassLoader();
        System.out.println(system);


    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException(name);
    }
}
