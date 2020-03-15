package cn.myframe.controller;


import cn.myframe.util.SSHData;
import cn.myframe.util.SshUtils;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.Data;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 15:36
 */
@Controller
public class SSHController {
    @Resource
    private SimpMessagingTemplate messagingTemplate ;
    static Map<String,SSHData> map = new HashMap<>();

    /**
     * 接收消息
     */
    @MessageMapping("/receive/{id}")
   // @SendTo("/topic/test")
    public String receiver(@DestinationVariable("id") String id, String msg)
            throws IOException {
        SSHData sshData = map.get(id);
        if(sshData != null){
            OutputStream outputStream = map.get(id).getOutputStream();
            outputStream.write((msg).getBytes());
            outputStream.flush();
        }else{
            messagingTemplate.convertAndSend("/topic/"+id,"远程服务器未连接。。。\n\r");
        }
        return msg;
    }

    /**
     * 建立SSH连接
     */
    @RequestMapping("/connect")
    @ResponseBody
    public String connect(String user,String host,Integer port,String password,String id)
            throws IOException {
        SSHData sshData = map.get(id);
        if(sshData != null){
            sshData.release();
        }
        ChannelShell channelShell = SshUtils.getShellChannel( user, host, port , password, id);
        if(channelShell == null){
            messagingTemplate.convertAndSend("/topic/"+id,
                    "远程服务器连接失败，请检查用户或者密码是正确\n\r");
            return "";
        }
        map.put(id,new SSHData(channelShell,messagingTemplate,id));
        return "";
    }

    /**
     * 断开连接
     */
    @RequestMapping("/disconnect")
    @ResponseBody
    public String disConnect(String id) throws IOException {
        SSHData sshData = map.get(id);
        if(sshData != null){
            sshData.release();
            map.remove(id);
        }
        messagingTemplate.convertAndSend("/topic/"+id,"已断开连接。。。\n\r");
        return "";
    }
}
