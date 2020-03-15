package cn.myframe.utils.mlib;

import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private double age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Person(){

    }

    public Person(String name, double age) {
        this.name = name;
        this.age = age;
    }
}
