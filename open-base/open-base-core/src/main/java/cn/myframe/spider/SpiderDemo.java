package cn.myframe.spider;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class SpiderDemo {

    private static Set<String> urlSet = new HashSet<String>();

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,5,30,TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000));

    /**
     * http://www.ngui.cc/a/material-pro4.1/material/form-mask.html
     */
    public static void main(String[] args) throws IOException {
        String url = "http://www.ngui.cc/a/material-pro4.1/material/icon-material.html";
        //dealUrl(url);
        //downLoad(url);
        dealPage(url);
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
            DomNodeList<DomElement> liList =  page.getElementsByTagName("li");
            for(DomElement dom : liList){
                DomNodeList<HtmlElement> aDomList = dom.getElementsByTagName("a");
                if(aDomList != null && aDomList.size()>0){
                    for(HtmlElement aDom: aDomList){
                        String menuListUrl =   aDom.getAttribute("href");
                       // System.out.println(menuListUrl);
                        if(menuListUrl.indexOf(".html")>0){
                           String downUrl =  dealUrl(menuListUrl);
                            Runnable runnable = ()->{
                                downLoad(downUrl);

                            };
                            threadPoolExecutor.execute(runnable);
                        }
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
            downLoadJsCss(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
       // System.out.println(page.asXml());

    }

    public static void downLoadJsCss(HtmlPage page){

        DomNodeList<DomElement> scriptList =  page.getElementsByTagName("script");
        for(DomElement dom:scriptList){
            try{
                String url = dom.getAttribute("src");
                dealUrl(url);
            }catch(Exception e){

            }

        }

        DomNodeList<DomElement> linkList =  page.getElementsByTagName("link");
        for(DomElement dom:linkList){
            try{
                String url = dom.getAttribute("href");
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
        String homeUrl = "http://www.ngui.cc/a/material-pro4.1/";
        if(url.contains("../")){
            url = url.replace("../",homeUrl);
        }else if(!url.contains("http")){
            url = homeUrl +"material/"+ url;
        }
        String dlPath = buildPath(url);
        if(dlPath != null){
            downloadFile(url,dlPath);
        }
        return url;
    }


    public static String buildPath(String url){
        if(StringUtils.isNotEmpty(url) && url.indexOf("http")>=0){
            String path = url.replace("http://", "");
            path = path.replace("https://", "");
            path = "D:/ngui/"+path;
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
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            if(remoteFilePath.indexOf("?")>=0){
                int urlindex = remoteFilePath.lastIndexOf("?");
                remoteFilePath = remoteFilePath.substring(0,urlindex);
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
