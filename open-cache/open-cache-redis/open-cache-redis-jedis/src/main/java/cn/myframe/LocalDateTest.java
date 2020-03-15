package cn.myframe;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Author: ynz
 * @Date: 2019/9/4/004 13:55
 * @Version 1.0
 */
public class LocalDateTest {

    public static void main(String[] args) {
        LocalDate date = LocalDate.now();
        System.out.println(date.until(LocalDate.of(2019,9,1),ChronoUnit.DAYS));
    }
}
