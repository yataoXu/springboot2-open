package cn.myframe.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *https://www.cnblogs.com/lyftest/p/6564282.html
 */
public class MmapExample {

    public static void main(String[] args) throws IOException {
        //testFileStream("F:/win/eclipse.zip");
        //testFileChannel("F:/win/eclipse.zip");
        testMappedByteBuffer("F:/win/eclipse.zip");
    }


    /**
     * 传统读写文件
     * @param path
     * @throws IOException
     */
    public static void testFileStream(String path)throws IOException {
        FileInputStream in = new FileInputStream(path);
        byte bytes[] = new byte[1024];
        long start = System.currentTimeMillis();//开始时间
        while (in.read(bytes) >0){
            bytes = new byte[1024];
        }
        in.close();
        long end = System.currentTimeMillis();//结束时间
        System.out.println("Read time: " + (end - start) + "ms");
    }

    /**
     * NIO
     * @param path
     * @throws IOException
     */
    public static void testFileChannel(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        FileChannel channel = file.getChannel();
        RandomAccessFile wfile = new RandomAccessFile("F:/eclipse.zip", "rw");
        FileChannel wchannel = wfile.getChannel();
        ByteBuffer buff = ByteBuffer.allocate(1024);
        long start = System.currentTimeMillis();//开始时间
        while (channel.read(buff) != -1) {
            buff.flip();
           // wchannel.write(buff);
            buff.clear();
        }
        long end = System.currentTimeMillis();
        System.out.println("Read time: " + (end - start) + "ms");
    }

    /**
     * NIO利用内存映射文件的方式读取文件
     * @param path
     * @throws IOException
     */
    public static void testMappedByteBuffer(String path) throws IOException {
        RandomAccessFile file = new RandomAccessFile(path, "rw");
        FileChannel fc = file.getChannel();
        RandomAccessFile wfile = new RandomAccessFile("F:/eclipse.zip", "rw");
        FileChannel wchannel = wfile.getChannel();
        int len = (int) file.length();
        MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_WRITE, 0, len);
        byte[] b = new byte[1024*1024];

        long start = System.currentTimeMillis();
        while (fc.read(buffer) != -1) {
            buffer.flip();
            wchannel.write(buffer);
            buffer.clear();
        }
        long end = System.currentTimeMillis();

        System.out.println("Read time: " + (end - start) + "ms");
    }



}
