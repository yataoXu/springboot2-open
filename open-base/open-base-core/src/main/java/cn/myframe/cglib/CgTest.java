package cn.myframe.cglib;

/**
 * <p/>
 *
 * @author cs12110 created at: 2019/1/2 16:47
 * <p>
 * since: 1.0.0
 */
public class CgTest {

    public static void main(String[] args) {
        CgLibUtil libUtil = new CgLibUtil();
        MyService proxy = libUtil.getProxy(MyService.class);
        proxy.say("123131");
      //  proxy.say("123131");
    }
}
