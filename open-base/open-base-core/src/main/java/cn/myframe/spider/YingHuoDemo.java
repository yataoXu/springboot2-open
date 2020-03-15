package cn.myframe.spider;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 爬取萤火小程序商城源码
 * https://demo.yiovo.com/index.php?s=/store/index/index
 * @Author: ynz
 * @Date: 2019/10/15/015 9:10
 * @Version 1.0
 */
public class YingHuoDemo {

    private static Set<String> urlSet = new HashSet<String>();

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,5,30,TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));

    public static void main(String[] args) {
        dealPage("https://demo.yiovo.com/index.php?s=/store/goods/add");
    }


    /**
     * 根据菜单下载
     * @param url
     */
    public static void dealPage(String url){
        WebClient webClient=new WebClient(BrowserVersion.CHROME);

        //设置Ajax控制器
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        //是否启用js
        webClient.getOptions().setActiveXNative(true);
        webClient.getOptions().setRedirectEnabled(true);
        //   webClient.getOptions().set
        webClient.getOptions().setJavaScriptEnabled(true);
        //是否启用css
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        //获取页面
        HtmlPage page = null;//登陆页面
        try {
            page = webClient.getPage(url);
            //  downLoadJsCss(page);

            DomNodeList<DomElement> liList =  page.getElementsByTagName("a");
            if(liList != null && liList.size()>0){
                for(DomElement aDom: liList){
                    String menuListUrl =   aDom.getAttribute("href");
                    // System.out.println(menuListUrl);
                    if(StringUtils.isNotEmpty(menuListUrl) && menuListUrl.indexOf(".php?")>0){
                        String downUrl =  dealUrl(menuListUrl);
                        Runnable runnable = ()->{
                            downLoad(downUrl);
                        };
                        threadPoolExecutor.execute(runnable);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单页面下载
     * @param url
     */
    public static void downLoad( String url) {

        WebClient webClient=new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        //设置Ajax控制器
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //是否启用js
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setActiveXNative(true);

        //   webClient.getOptions().set
        webClient.getOptions().setJavaScriptEnabled(true);
        //是否启用css
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        //获取页面
        HtmlPage page = null;//登陆页面
        try {
            page = webClient.getPage(url);
            downLoadJsCss(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(page.asXml());

    }

    public static void downLoadJsCss(HtmlPage page){

        DomNodeList<DomElement> linkList =  page.getElementsByTagName("link");
        for(DomElement dom:linkList){
            try{
                String url = dom.getAttribute("href");
                dealUrl(url);
            }catch(Exception e){

            }
        }

        DomNodeList<DomElement> scriptList =  page.getElementsByTagName("script");
        for(DomElement dom:scriptList){
            try{
                String url = dom.getAttribute("src");
                dealUrl(url);
            }catch(Exception e){

            }

        }




        DomNodeList<DomElement> imgList =  page.getElementsByTagName("img");
        for(DomElement dom:imgList){
            try{
                String url = dom.getAttribute("src");
                dealUrl(url);

            }catch(Exception e){

            }

        }

        DomNodeList<DomElement> metaList =  page.getElementsByTagName("meta");
        for(DomElement dom:metaList){
            try{
                String url = dom.getAttribute("href");
                dealUrl(url);
            }catch(Exception e){

            }

        }
    }

    public static String dealUrl(String url){
        String homeUrl = "https://demo.yiovo.com/";
        url = homeUrl + url;
        String dlPath = buildPath(url);
        if(dlPath != null && !url.contains("php")){
            downloadFile(url,dlPath);
        }
        return url;
    }


    public static String buildPath(String url){
        if(StringUtils.isNotEmpty(url) && url.indexOf("http")>=0){
            String path = url.replace("http://", "");
            path = path.replace("https://", "");
            path = "D:/YingHuo/"+path;
            return path;
        }

        return null;

    }

    public static void downloadFile(String remoteFilePath, String localFilePath){
        System.out.println("下载文件："+localFilePath);
        if(!urlSet.contains(remoteFilePath)){
            urlSet.add(remoteFilePath);
            URL urlfile = null;
            HttpURLConnection httpUrl = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            if(remoteFilePath.indexOf("?")>=0){
                int urlindex = remoteFilePath.lastIndexOf("?");
                remoteFilePath = remoteFilePath.substring(0,urlindex);

                int urlindex2 = localFilePath.lastIndexOf("?");
                localFilePath = localFilePath.substring(0,urlindex2);
            }
            int lastIndex = localFilePath.lastIndexOf("/");
            String path = localFilePath.substring(0,lastIndex);
            File pathFile = new File(path);
            if(!pathFile.exists()){
                pathFile.mkdirs();
            }
            File f = new File(localFilePath);

            try
            {
                urlfile = new URL(remoteFilePath);
                System.out.println("");
                httpUrl = (HttpURLConnection)urlfile.openConnection();
                httpUrl.connect();
                bis = new BufferedInputStream(httpUrl.getInputStream());
                bos = new BufferedOutputStream(new FileOutputStream(f));
                int len = 2048;
                byte[] b = new byte[len];
                while ((len = bis.read(b)) != -1)
                {
                    bos.write(b, 0, len);
                }
                System.out.println("下载成功");
                bos.flush();
                bis.close();
                httpUrl.disconnect();
            }
            catch (Exception e)
            {
                //e.printStackTrace();
            }
            finally
            {
                try
                {
                    bis.close();
                    bos.close();
                }
                catch (IOException e)
                {
                    //  e.printStackTrace();
                }
            }
        }

    }


}
