package cn.myframe.test;

import java.util.Observable;
import java.util.Observer;

/**
 * @Author: ynz
 * @Date: 2019/11/12/012 16:03
 * @Version 1.0
 */
public class Test6 {

    private static class Student implements Observer {

        @Override
        public void update(Observable o, Object arg) {

            System.out.println(1);
        }
    }

    private static class Student2 implements Observer {

        @Override
        public void update(Observable o, Object arg) {

            System.out.println(2+String.valueOf(arg));
        }
    }

    private static class Teacher extends Observable {

        public void updateStatus(){
            setChanged();
            notifyObservers("1");
        }

    }

    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        teacher.addObserver(new Student());
        teacher.addObserver(new Student2());
        teacher.updateStatus();
    }
}
