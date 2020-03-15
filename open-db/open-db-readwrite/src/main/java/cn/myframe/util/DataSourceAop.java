package cn.myframe.util;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(cn.myframe.util.annotation.Master) " +
            "&& (execution(* cn.myframe.service..*.select*(..)) " +
            "||  execution(* cn.myframe.service..*.find*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(cn.myframe.util.annotation.Master) " +
            "|| execution(* cn.myframe.service..*.insert*(..)) ")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }
}
