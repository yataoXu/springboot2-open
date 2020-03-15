package cn.myframe.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@RestController
public class ErrorController {
    /**
     * 随机抛出异常.
     */
    private void randomException() throws Exception {
        Exception[] exceptions = { //异常集合
                new NullPointerException(),
                new ArrayIndexOutOfBoundsException(),
                new NumberFormatException(),
                new SQLException()};
        //发生概率
        double probability = 0.75;
        if (Math.random() < probability) {
            //情况1：要么抛出异常
            throw exceptions[(int) (Math.random() * exceptions.length)];
        } else {
            //情况2：要么继续运行
        }

    }

    /**
     * 模拟用户数据访问.
     */
    @RequestMapping("/exception")
    public List index() throws Exception {
        randomException();
        return Arrays.asList("正常用户数据1!", "正常用户数据2! 请按F5刷新!!");
    }


}
