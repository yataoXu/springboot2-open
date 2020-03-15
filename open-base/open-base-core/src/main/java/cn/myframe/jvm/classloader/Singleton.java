package cn.myframe.jvm.classloader;

/**
 * @Author: ynz
 * @Date: 2019/10/12/012 9:39
 * @Version 1.0
 */
public class Singleton {

    private volatile Singleton singleton = null;

    public Singleton getSingleton(){
        if(singleton == null){
            synchronized( Singleton.class ){
                if( singleton == null ){
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
