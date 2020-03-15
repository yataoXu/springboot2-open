package cn.myframe.demo;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeSet;
import java.util.concurrent.locks.LockSupport;

/**
 * @Author: ynz
 * @Date: 2019/11/25/025 15:08
 * @Version 1.0
 */
@Slf4j
public class D3 {
    public static void main(String[] args) {

        System.out.println( Math.ceil(11.1));
    }




    public void s(){
        int a;

    }
}

@Data
@AllArgsConstructor
class Person implements Comparable {
    String name;
    int age;

    @Override
    public int compareTo(Object o) {
        Person person = (Person)o;
        if(this.age > person.getAge()){
            return -1;
        }
        return 1;

    }
}
