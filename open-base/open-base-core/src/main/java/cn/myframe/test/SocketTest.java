package cn.myframe.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Author: ynz
 * @Date: 2019/8/22/022 9:27
 * @Version 1.0
 */
public class SocketTest {

    public static void main(String[] args) throws IOException {

        try(Socket socket = new Socket("10.10.2.139",7003)){
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            os.write("set hello hello-redis\r\n".getBytes());

            //从redis服务器读,到bytes中
            byte[] bytes = new byte[1024];
            int len = is.read(bytes);

            // to string 输出一下
            System.out.println(new String(bytes,0,len));
        }


    }
}
