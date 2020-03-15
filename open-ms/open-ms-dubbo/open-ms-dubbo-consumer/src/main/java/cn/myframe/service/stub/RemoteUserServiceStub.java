package cn.myframe.service.stub;

import cn.myframe.service.RemoteUserService;

public class RemoteUserServiceStub implements RemoteUserService {

    private final RemoteUserService remoteUserService;

    /**
     * 有参构造 dubbo会自动将远程remoteUserService注入进来
     * @param remoteUserService
     */
    public RemoteUserServiceStub(RemoteUserService remoteUserService) {
        this.remoteUserService = remoteUserService;
    }

    @Override
    public String sayHello(String name) {
        return remoteUserService.sayHello(name)+";stub";
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
