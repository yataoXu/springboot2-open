package cn.myframe;

import redis.clients.jedis.Jedis;

import java.text.NumberFormat;

/**
 * @Author: ynz
 * @Date: 2019/9/4/004 10:39
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
        int count = 10000;
        Jedis jedis = new Jedis("10.10.2.134",6379);

        String[] ele = new String[10000];
        for (int i = 0; i < count; i++) {
            ele[i%10000] = "user_" + i;
            if( (i+1)%10000 == 0 || count == i+1 ){
                jedis.pfadd("codehole", ele);
            }
        }
        long total = jedis.pfcount("codehole");
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float)Math.abs(total -count )/ (float) count * 100);
        System.out.printf("%d %d %s", count, total,result+"%");
        jedis.close();
    }
}
