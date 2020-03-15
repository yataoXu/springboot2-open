package cn.myframe.service.eventnotity;

import cn.myframe.service.pojo.Project;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 10:04
 * @Version 1.0
 */
@Component
public class NotifyImpl implements Notify {

    public Map<String, Project>    ret    = new HashMap<String, Project>();
    public Map<String, Throwable> errors = new HashMap<String, Throwable>();

    public void onreturn(Project project, String name) {
        System.out.println("onreturn:" + project+",name:"+name);
        ret.put(name, project);
    }
    public void onthrow(Throwable ex, String name) {
        errors.put(name, ex);
    }
}
