package cn.myframe.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.word.Word07Writer;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;

import java.awt.*;

/**
 * @Author: ynz
 * @Date: 2019/9/6/006 9:22
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {
        Word07Writer writer = new Word07Writer();

// 添加段落（标题）
        writer.addText(new Font("方正小标宋简体", Font.PLAIN, 22), "我是第一部分", "我是第二部分");
// 添加段落（正文）
        writer.addText(new Font("宋体", Font.PLAIN, 22), "我是正文第一部分", "我是正文第二部分");
// 写出到文件
        writer.flush(FileUtil.file("e:/wordWrite.docx"));
// 关闭
        writer.close();
    }

}
