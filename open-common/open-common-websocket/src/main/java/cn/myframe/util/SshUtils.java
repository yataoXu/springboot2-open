package cn.myframe.util;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Properties;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 19:49
 */
public class SshUtils {

    public static ChannelShell getShellChannel(@RequestParam String user, @RequestParam String host,
                                        @RequestParam Integer port , @RequestParam String password,
                                        @RequestParam String id){
        ChannelShell channel = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(user, host, port);
            Session session = jsch.getSession(user, host, port);
            //log.info("Session created.");
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            channel = (ChannelShell) session.openChannel("shell");
            channel.setEnv("LANG", "zh_CN.UTF8");
            channel.setAgentForwarding(false);
            channel.connect();
            channel.setPtySize(500, 500, 1000, 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return channel;
    }
}
