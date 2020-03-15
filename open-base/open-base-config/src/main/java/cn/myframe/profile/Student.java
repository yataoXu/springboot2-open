package cn.myframe.profile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2018/12/24/024 18:09
 * @Version 1.0
 */
@Component
@Profile("test")
@Slf4j
public class Student implements Person{
    @Override
    public void say() {
        log.info("我是学生");
    }
}
