package cn.myframe.demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author: ynz
 * @Date: 2019/2/15/015 14:43
 * @Version 1.0
 */
public class Demo {

    public static void  a(){
        boolean b=true?false:true==true?false:true;
        System.out.println(b);
    }

    public static void main(String[] args) {
        Demo b = null;
        b.a();
    }


    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
       return true;
    }
}

class B extends CE{
    public B(String s){
        super("E");
        System.out.print("B");
    }
}

class CE{
    public CE(){
        System.out.print("C");
    }

    public CE(String e){
        System.out.print(e);
    }

}