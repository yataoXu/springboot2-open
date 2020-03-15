package cn.myframe.service;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 11:57
 * @Version 1.0
 */
public class MockUserServicImpl implements RemoteUserService {
    @Override
    public String sayHello(String name) {
        return "error";
    }

    @Override
    public String delaySayHello(Long delayTime) {
        return null;
    }

    @Override
    public String attachment() {
        return null;
    }
}
