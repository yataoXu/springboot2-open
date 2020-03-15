package cn.myframe.service.eventnotity;

import cn.myframe.service.pojo.Project;

/**
 * @Author: ynz
 * @Date: 2019/1/4/004 10:03
 * @Version 1.0
 */
public interface Notify {
    public void onreturn(Project project, String name);
    public void onthrow(Throwable ex, String name);
}
