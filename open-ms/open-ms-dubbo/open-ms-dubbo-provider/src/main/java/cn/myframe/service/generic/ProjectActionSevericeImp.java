package cn.myframe.service.generic;

import cn.myframe.service.pojo.Project;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 13:44
 * @Version 1.0
 */
@Component
//@Service(version = "2.7.0",timeout = 10000,interfaceClass = ActionService.class)
public class ProjectActionSevericeImp implements ActionService<Project> {

    @Override
    public Project query() {
        return null;
    }

    @Override
    public String save(Project project) {
        return "project";
    }
}
