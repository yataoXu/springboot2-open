package cn.myframe.service;

public interface MailService {

    void sendSimpleMail(String from,String to, String subject, String content);

    public void sendHtmlMail(String from,String to, String subject, String content);

    public void sendAttachmentsMail(String from,String to, String subject, String content, String filePath);

    public void sendInlineResourceMail(String from,String to, String subject, String content, String rscPath, String rscId);
}
