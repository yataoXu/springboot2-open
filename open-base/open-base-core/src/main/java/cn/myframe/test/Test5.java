package cn.myframe.test;

import java.math.BigDecimal;

/**
 * @Author: ynz
 * @Date: 2019/11/12/012 14:25
 * @Version 1.0
 */
public class Test5 {

    public static void main(String[] args) {
        cal(100D,10);
    }

    public static void cal(double money,int count){
        BigDecimal totalDec = new BigDecimal(0);
        BigDecimal moneyDec = new BigDecimal(money);
        for(int i = 0;i<count;i++){
            if(i !=  count -1){
                //count剩余个数
                BigDecimal countDec = new BigDecimal(count-i);
                //  money*2/count           money剩余金额     count剩余个数
                BigDecimal div = moneyDec.multiply(new BigDecimal(2))
                        .divide(countDec,2,5);
                // 获取随机数
                BigDecimal randomDec = new BigDecimal(Math.random()*(div.floatValue()))
                        .setScale(2,5);
                //money剩余金额
                moneyDec = moneyDec.subtract(randomDec);
                totalDec = totalDec.add(randomDec);
            }else{
                totalDec = totalDec.add(moneyDec);
            }

        }
        System.out.println(totalDec.floatValue());
    }
}
