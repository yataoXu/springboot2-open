package cn.myframe.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

/**
 * @Author: ynz
 * @Date: 2019/5/11/011 11:38
 * @Version 1.0
 */

@SpringBootTest(classes = {SpringBootApplication.class})
public class BaseTest extends AbstractTestNGSpringContextTests {


}
