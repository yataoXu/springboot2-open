package cn.myframe.util;

import com.jcraft.jsch.ChannelShell;
import lombok.Data;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: ynz
 * @Date: 2018/12/22/022 19:59
 */
@Data
public class SSHData {
    static ExecutorService executor = Executors.newCachedThreadPool();

    ChannelShell channel;
    Future<String> future;
    private InputStream inputStream;
    private OutputStream outputStream;

    public SSHData(ChannelShell channel, SimpMessagingTemplate messagingTemplate, String id){
        this.channel = channel;
        try {
            this.inputStream = channel.getInputStream();
            this.outputStream = channel.getOutputStream();
            this.future = executor.submit(() -> {
                byte[] tmp=new byte[1024];
                while (true){
                    try{
                        while(inputStream.available()>0){
                            int i=inputStream.read(tmp, 0, 1024);
                            if(i<0)break;
                            messagingTemplate.convertAndSend("/topic/"+id,new String(tmp, 0, i));
                        }
                    }catch (Exception e){
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        try{
            future.cancel(true);
            inputStream.close();
            outputStream.close();
            this.channel.disconnect();
        }catch (Exception  e){

        }

    }
}
