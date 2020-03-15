package cn.myframe.cache;

import cn.myframe.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 7:59
 */
@Component
@Slf4j
public class CacheUtil {

    @Autowired
    User user;

    @Cacheable(value = "user", key = "#user.name")
    public User getUser(User user){
        log.info("get user");
        return user;
    }

    @CachePut(value = "user", key = "#user.name")
    public User saveUser(User user){
        log.info("save user");
        return user;
    }

    @CacheEvict(value = "user", key = "#name") // 移除指定key的数据
    public void deleteUser(String name){
        log.info("delete user");
    }

    @CacheEvict(value = "user", allEntries = true) // 移除所有数据
    public void deleteAll() {
        log.info("delete All");
    }

}
