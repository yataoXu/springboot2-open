package cn.myframe.test;

import java.util.Objects;

/**
 * @Author: ynz
 * @Date: 2019/8/12/012 8:39
 * @Version 1.0
 */
public class TestExtends implements test,test2{

    public static void main(String[] args) {
        if(Objects.equals("abc","abc")){
            System.out.println(123);

        }

    }

    @Override
    public void j() {

    }
}

interface test{

    default void j(){
        System.out.println("abc");
    };

}

interface test2{
    default void j(){
        System.out.println("bcd");
    };
}

