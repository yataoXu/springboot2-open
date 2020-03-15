package cn.myframe.service.eventnotity;

import cn.myframe.service.RemoteUserService;
import cn.myframe.service.pojo.Project;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 9:57
 * @Version 1.0
 */
@Component
//@Service(version = "2.7.0",timeout = 10000,interfaceClass = IDemoService.class)
public class NormalDemoService implements IDemoService{
    @Override
    public Project get(String projectName) {
        return new Project(projectName);
    }
}
