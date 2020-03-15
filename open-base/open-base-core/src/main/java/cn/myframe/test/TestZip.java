package cn.myframe.test;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: ynz
 * @Date: 2019/8/21/021 9:02
 * @Version 1.0
 */
public class TestZip {

    public static void main(String[] args) {
        zipFileChannel();
        zipFileMap();
    }

    public static void zipFileNoBuffer() {
        File zipFile = new File("E://t.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //开始时间
            long beginTime = System.currentTimeMillis();

            for (int i = 0; i < 10; i++) {
                try (InputStream input = new FileInputStream("E://t.jpg")) {
                    zipOut.putNextEntry(new ZipEntry( i+".jpg"));
                    int temp = 0;
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
            //printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void zipFileBuffer() {
        File zipFile = new File("E://t1.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut)) {
            //开始时间
            long beginTime = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("E://t.jpg"))) {
                    zipOut.putNextEntry(new ZipEntry(i+".jpg"));
                    int temp = 0;
                    while ((temp = bufferedInputStream.read()) != -1) {
                        bufferedOutputStream.write(temp);
                    }
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFileChannel() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File("E://t2.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                File file = new File("E://t.jpg");
                try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
                    zipOut.putNextEntry(new ZipEntry(i + ".jpg"));
                    fileChannel.transferTo(0, file.length(), writableByteChannel);
                }
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Version 4 使用Map映射文件
    public static void zipFileMap() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File("E://t3.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            File file = new File("E://t.jpg");
            for (int i = 0; i < 10; i++) {

                zipOut.putNextEntry(new ZipEntry(i + ".jpg"));

                //内存中的映射文件
                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(file, "r").getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, file.length());

                writableByteChannel.write(mappedByteBuffer);
            }
            System.out.println(System.currentTimeMillis() - beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
