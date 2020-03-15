package cn.myframe.controller;

import cn.myframe.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Author: ynz
 * @Date: 2018/12/23/023 22:20
 */
@RestController
public class MainController {


    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.sendTo}")
    private String sendTo;

    @RequestMapping("/send")
    public String send(){
        sendSimpleMail();
        return "success";
    }

    public void sendSimpleMail(){
        mailService.sendSimpleMail(from,sendTo,"这是一封测试邮件！","这是一封测试邮件！");
    }

    public void sendHtmlMail() throws Exception {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail(from,sendTo,"test simple mail",content);
    }

    public void sendAttachmentsMail() {
        String filePath="e:\\tmp\\application.log";
        mailService.sendAttachmentsMail(from,sendTo, "主题：带附件的邮件", "有附件，请查收！", filePath);
    }


    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = "C:\\Users\\summer\\Pictures\\favicon.png";

        mailService.sendInlineResourceMail(from,sendTo,"主题：这是有图片的邮件", content, imgPath, rscId);
    }


    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("MailTemplate", context);
        mailService.sendHtmlMail(from,sendTo,"主题：这是模板邮件",emailContent);
    }
}
