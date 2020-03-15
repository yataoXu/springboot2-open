package cn.myframe.controller;

import cn.myframe.server.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 10:38
 */
@Controller
public class CheckCenterController {

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,@RequestBody String message) {
        try {
            WebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
