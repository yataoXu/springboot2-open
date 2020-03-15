package cn.myframe.controller;

import cn.myframe.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2018/12/24/024 9:05
 * @Version 1.0
 */
@RestController
@Slf4j
public class LogController {
    /**
     * 测试日志等级
     * @return
     */
    @RequestMapping("/loglevel")
    public String logLevel(){
        log.debug("debug log");
        log.info("info log");
        log.error("error log");
        return "success";
    }

    /**
     * 设置日志等级
     * @param level
     * @param packages
     * @return
     */
    @RequestMapping("/changeLog/{level}")
    public String changeLevel(@PathVariable String level, @RequestBody String packages){
        if(StringUtils.isEmpty(packages)){
            LogUtil.setAllLogLevel(level);
        }else{
            LogUtil.setLogLevel(packages,level);
        }
        return "success";
    }
}
