package cn.myframe.ms;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: ynz
 * @Date: 2019/1/16/016 13:14
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class Example_3ThreadLoacl {

    public String name;

    public static void main(String[] args) throws InterruptedException {
        Example_3ThreadLoacl example_3ThreadLoacl = new Example_3ThreadLoacl("main");
        ThreadLocal<String> threadLoacl = new ThreadLocal<String>();
        threadLoacl.set(new String("main"));
        new Thread(()->{
            /*threadLoacl.set(example_3ThreadLoacl);
            Example_3ThreadLoacl threadLoacl1 = threadLoacl.get();
            threadLoacl1.setName("thread1");
            threadLoacl.set(threadLoacl1);*/
            //threadLoacl.set(new String("thread1"));
          //  System.out.println(this==Thread.currentThread());
        }).start();

        Thread.sleep(1000);
/*
        new Thread(()->{
            threadLoacl.set(example_3ThreadLoacl);
            Example_3ThreadLoacl threadLoacl1 = threadLoacl.get();
            threadLoacl1.setName("thread2");
            threadLoacl.set(threadLoacl1);
        }).start();

        Thread.sleep(1000);

        new Thread(()->{
            threadLoacl.set(example_3ThreadLoacl);
            Example_3ThreadLoacl example3 = threadLoacl.get();
            System.out.println(example3.name);
            example3.setName("thread3");
            threadLoacl.set(example3);
        }).start();

        Thread.sleep(1000);*/

        System.out.println(Thread.currentThread());
        System.out.println(threadLoacl.get()/*.getName()*/);


        System.out.println(hammingWeight(Integer.MAX_VALUE));

        Integer i = 1;
        int i2 = 1;
        Integer i3 = 127;
        Integer i8 = new Integer(129);
        int i4 =127;
        Integer i5 = 127;
        System.out.println(i==i2);
        System.out.println(i3 == i8.intValue());
        Integer i9 = new Integer(127);
        System.out.println(i3== i5);

        System.out.println("-------------------------");
        compareInt();

    }

    public static int hammingWeight(int n){
        int count = 0;
        for(int i=0;i<32;i++){
            if((n&1) == 1){
                count++;
            }
            n = n>>1;
        }
        return count;
    }


    public static void compareInt(){
        Integer intger1 = 1;
        Integer integer2 = 1;
        int i1 = 1;
        Integer integerNew1 = new Integer(1);

        //true
        //int和integer(无论new否)比，都为true，因为会把Integer自动拆箱为int再去比
        System.out.println(intger1 == i1);
        //false
        //-128到127之间的数,(Integer i =127)专门存放他的内存（常量池）
        // new Integer(1)只会引用指向堆
        System.out.println(intger1 == integerNew1);
        //true
        //都是常量池中的值
        System.out.println(intger1 == integer2);

        Integer integer1000 = 1000;
        Integer integer2000 = 1000;
        Integer integerNew1000 = new Integer(1000);
        int i1000 = 1000;

        //true
        System.out.println(integer1000 == i1000);
        //false
        //都指向不同的堆地址
        System.out.println(integer1000 == integerNew1000);
        //false
        //都指向不同的堆地址
        System.out.println(integer1000 == integer2000);

    }
}
