package cn.myframe.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/8/21/021 14:28
 * @Version 1.0
 */
public class Test2 {

   static ArrayList<String> list = new ArrayList<String>();

    public static void main(String[] args) {

        for (int i = 0; i < 23; i++) {
            list.add("s"+i);
            list.clear();
        }


        System.out.println(10>>1);
        /*//自定义一个类加载器
        ClassLoader myLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                try {
                    String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                    InputStream is = getClass().getResourceAsStream(fileName);
                    if (is == null) {
                        return super.loadClass(name);
                    }
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes); //通过自定义类加载器读取class文件的二进制流
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException(name);
                }
            }
        };


        try {
            Object obj = myLoader.loadClass("cn.myframe.test.Test2").newInstance();
            System.out.println(obj instanceof Test2);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
*/
    }
}
