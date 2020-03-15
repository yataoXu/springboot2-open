package cn.myframe.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Author: ynz
 * @Date: 2019/8/21/021 9:37
 * @Version 1.0
 */
public class ZipUtils {

    /**
     * 压缩
     * @param file       要压缩的文件或者目录
     * @param zipFile    压缩后的文件(.zip)
     */
    private static void zip(File file,File zipFile){
        Map<File,String> fileMap = new HashMap<>();
        getAllFile( file, fileMap);
        zip(zipFile,fileMap);
    }


    /**
     * 迭代文件
     * @param file     文件或者目录
     * @param fileMap
     */
    private static void getAllFile(File file, Map<File,String> fileMap){
        getAllFile( file,  fileMap,"");
    }


    /**
     * 迭代文件
     * @param file     文件或者目录
     * @param fileMap
     * @param path
     */
    private static void getAllFile(File file, Map<File,String> fileMap,String path) {
        if (file == null || !file.exists()) {
            new IllegalArgumentException("文件不存在");
        }
        if (fileMap == null) {
            fileMap = new HashMap<>();
        }
        if (file.isFile()) {
            fileMap.put(file,path.isEmpty() ? file.getName() : path + File.separator + file.getName());
        }
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (File sonFile : listFile) {
                getAllFile(sonFile, fileMap, path.isEmpty() ? file.getName() : path + File.separator + file.getName());
            }
        }
    }


    /**
     * 压缩文件
     * @param zipFile
     * @param fileMap
     */
    public static void zip(File zipFile,Map<File,String> fileMap){
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for(Map.Entry<File,String> entry : fileMap.entrySet()){
                File file = entry.getKey();
                try (FileChannel fileChannel = new FileInputStream(file).getChannel()) {
                    zipOut.putNextEntry(new ZipEntry(entry.getValue()));
                    fileChannel.transferTo(0, file.length(), writableByteChannel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String zipFileName, String dstPath){
        ZipEntry zipEntry = null;
        try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileName))){
            while ((zipEntry = zipInputStream.getNextEntry()) != null){
                if (zipEntry.isDirectory()) {//若是zip条目目录，则需创建这个目录
                    File dir = new File(dstPath + File.separator + zipEntry.getName());
                    if (!dir.exists()) {
                        dir.mkdirs();
                        continue;
                    }
                }
            }

        }catch (Exception e){

        }
    }



    public static void main(String[] args) {

        Map<File,String>  fileMap = new HashMap<>();;
        getAllFile(new File("E:\\abc.txt"), fileMap);

        zip(new File("E://zip.zip"),fileMap);
       // System.out.println(list.size());

    }
}
