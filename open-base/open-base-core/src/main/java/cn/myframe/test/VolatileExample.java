package cn.myframe.test;// 以下代码来源于【参考 1】

class VolatileExample {

  int a = 2;

  public static void main(String[] args) {
    VolatileExample e = new VolatileExample();
    Thread t = new Thread(()->{
      e.a =3;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    });

    Thread t2 = new Thread(()->{
      System.out.println(e.a);
    });
    t2.start();
    t.start();


  }


}