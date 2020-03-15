package cn.myframe.service.generic;

import cn.myframe.service.pojo.User;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * @Author: ynz
 * @Date: 2019/1/3/003 13:44
 * @Version 1.0
 */
@Service(version = "2.7.0",timeout = 10000,interfaceClass = ActionService.class)
public class UserActionSevericeImp implements ActionService<User> {

    @Override
    public User query() {
        return null;
    }

    @Override
    public String save(User user) {
        return "User";
    }
}
