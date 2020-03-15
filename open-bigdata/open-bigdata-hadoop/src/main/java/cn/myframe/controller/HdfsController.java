package cn.myframe.controller;

import cn.myframe.config.HadoopTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/1/28/028 14:48
 * @Version 1.0
 */
@RestController
public class HdfsController {

    @Autowired
    private HadoopTemplate hadoopTemplate;

    /**
     * 将本地文件srcFile,上传到hdfs
     * @param srcFile
     * @return
     */
    @RequestMapping("/upload")
    public String upload(@RequestParam String srcFile){
        hadoopTemplate.uploadFile(srcFile);
        return "copy";
    }

    @RequestMapping("/delFile")
    public String del(@RequestParam String fileName){
        hadoopTemplate.delFile(fileName);
        return "delFile";
    }

    @RequestMapping("/download")
    public String download(@RequestParam String fileName,@RequestParam String savePath){
        hadoopTemplate.download(fileName,savePath);
        return "download";
    }
}
