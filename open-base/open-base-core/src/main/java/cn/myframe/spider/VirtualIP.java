package cn.myframe.spider;

import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ynz
 * @Date: 2019/4/25/025 8:56
 * @Version 1.0
 */
@Slf4j
public class VirtualIP {

    static HtmlPage htmlPage = null;

    static WebClient webClient= null;

    static String listUrl = "https://blog.csdn.net/cowbin2012/article/list/NumPage?";

    static String[] arts = {"https://blog.csdn.net/cowbin2012/article/details/89495190","https://blog.csdn.net/cowbin2012/article/details/89362508","https://blog.csdn.net/cowbin2012/article/details/89338738","https://blog.csdn.net/cowbin2012/article/details/89318005","https://blog.csdn.net/cowbin2012/article/details/86713138","https://blog.csdn.net/cowbin2012/article/details/86711362","https://blog.csdn.net/cowbin2012/article/details/86710812","https://blog.csdn.net/cowbin2012/article/details/86685543","https://blog.csdn.net/cowbin2012/article/details/86680202","https://blog.csdn.net/cowbin2012/article/details/86673812","https://blog.csdn.net/cowbin2012/article/details/86671927","https://blog.csdn.net/cowbin2012/article/details/86494997","https://blog.csdn.net/cowbin2012/article/details/86376105","https://blog.csdn.net/cowbin2012/article/details/85777055","https://blog.csdn.net/cowbin2012/article/details/85407495","https://blog.csdn.net/cowbin2012/article/details/85337250","https://blog.csdn.net/cowbin2012/article/details/85330585","https://blog.csdn.net/cowbin2012/article/details/85298975","https://blog.csdn.net/cowbin2012/article/details/85297547","https://blog.csdn.net/cowbin2012/article/details/85290876","https://blog.csdn.net/cowbin2012/article/details/85289248","https://blog.csdn.net/cowbin2012/article/details/85266174","https://blog.csdn.net/cowbin2012/article/details/85256360","https://blog.csdn.net/cowbin2012/article/details/85252433","https://blog.csdn.net/cowbin2012/article/details/85251655","https://blog.csdn.net/cowbin2012/article/details/85251524","https://blog.csdn.net/cowbin2012/article/details/85250855","https://blog.csdn.net/cowbin2012/article/details/85248381","https://blog.csdn.net/cowbin2012/article/details/85247875","https://blog.csdn.net/cowbin2012/article/details/85247333","https://blog.csdn.net/cowbin2012/article/details/85247053","https://blog.csdn.net/cowbin2012/article/details/85237857","https://blog.csdn.net/cowbin2012/article/details/85237760","https://blog.csdn.net/cowbin2012/article/details/85237502","https://blog.csdn.net/cowbin2012/article/details/85235931","https://blog.csdn.net/cowbin2012/article/details/85220055","https://blog.csdn.net/cowbin2012/article/details/85220046","https://blog.csdn.net/cowbin2012/article/details/85220036","https://blog.csdn.net/cowbin2012/article/details/85220033","https://blog.csdn.net/cowbin2012/article/details/85219887","https://blog.csdn.net/cowbin2012/article/details/85194555","https://blog.csdn.net/cowbin2012/article/details/85194492","https://blog.csdn.net/cowbin2012/article/details/85194460","https://blog.csdn.net/cowbin2012/article/details/85194431","https://blog.csdn.net/cowbin2012/article/details/85194393","https://blog.csdn.net/cowbin2012/article/details/85194353","https://blog.csdn.net/cowbin2012/article/details/89405220","https://blog.csdn.net/cowbin2012/article/details/89397457","https://blog.csdn.net/cowbin2012/article/details/89335693","https://blog.csdn.net/cowbin2012/article/details/89315055","https://blog.csdn.net/cowbin2012/article/details/89305542","https://blog.csdn.net/cowbin2012/article/details/88888115","https://blog.csdn.net/cowbin2012/article/details/88861633","https://blog.csdn.net/cowbin2012/article/details/88861160","https://blog.csdn.net/cowbin2012/article/details/86689902","https://blog.csdn.net/cowbin2012/article/details/86689565","https://blog.csdn.net/cowbin2012/article/details/86685390","https://blog.csdn.net/cowbin2012/article/details/86654356","https://blog.csdn.net/cowbin2012/article/details/85337413","https://blog.csdn.net/cowbin2012/article/details/85254990","https://blog.csdn.net/cowbin2012/article/details/85242177","https://blog.csdn.net/cowbin2012/article/details/81147503","https://blog.csdn.net/cowbin2012/article/details/80740628","https://blog.csdn.net/cowbin2012/article/details/80717534","https://blog.csdn.net/cowbin2012/article/details/80498835","https://blog.csdn.net/cowbin2012/article/details/80034905","https://blog.csdn.net/cowbin2012/article/details/80018905","https://blog.csdn.net/cowbin2012/article/details/79997742","https://blog.csdn.net/cowbin2012/article/details/79997612","https://blog.csdn.net/cowbin2012/article/details/79940606","https://blog.csdn.net/cowbin2012/article/details/47265553","https://blog.csdn.net/cowbin2012/article/details/47204961","https://blog.csdn.net/cowbin2012/article/details/47112603","https://blog.csdn.net/cowbin2012/article/details/47111323","https://blog.csdn.net/cowbin2012/article/details/47067089","https://blog.csdn.net/cowbin2012/article/details/47066949","https://blog.csdn.net/cowbin2012/article/details/47066839","https://blog.csdn.net/cowbin2012/article/details/33342893","https://blog.csdn.net/cowbin2012/article/details/29929485","https://blog.csdn.net/cowbin2012/article/details/22792757","https://blog.csdn.net/cowbin2012/article/details/10200201","https://blog.csdn.net/cowbin2012/article/details/10053543","https://blog.csdn.net/cowbin2012/article/details/10035011","https://blog.csdn.net/cowbin2012/article/details/8827575","https://blog.csdn.net/cowbin2012/article/details/8827366","https://blog.csdn.net/cowbin2012/article/details/8826514","https://blog.csdn.net/cowbin2012/article/details/8826351","https://blog.csdn.net/cowbin2012/article/details/8818829"};
    static  ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(200, 300, 30, TimeUnit.SECONDS,new ArrayBlockingQueue<>(5000));
    static List<String> artList = new ArrayList<String>();

  //  static List<String> repeatIps = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        for (int i = 1; i <=5 ; i++) {
            String url = listUrl.replaceAll("NumPage",String.valueOf(i));
            artical(url);

        }
        arts = artList.toArray(new String[artList.size()]);

      /*  while(true){
            System.out.println(JSON.toJSONString(artList));
            String[] vIps = viewIPPage("http://www.89ip.cn/tqdl.html?api=1&num=9999&port=&address=&isp=");
            if(vIps != null){
                for(String ip : vIps){
                    concurrentViewCsdn( ip, arts[new Random().nextInt(88)]);
                }
            }
            while(threadPoolExecutor.getActiveCount() != 0){
                Thread.sleep(1000);
            }
        }*/

        System.out.println(JSON.toJSONString(artList));
        for (int i = 1; i < 500; i++) {
            String[] vIps = viewIPPage2("https://www.xicidaili.com/nt/"+i);
            if(vIps != null){
                for(String ip : vIps){
                    concurrentViewCsdn( ip, arts[new Random().nextInt(arts.length)]);
                }
            }
            while(threadPoolExecutor.getActiveCount() != 0){
                Thread.sleep(1000);
            }
        }




    }

    public static void concurrentViewCsdn(String ip, String url){
        try{
            if(ip.contains(":")){
                String[] ipAndPort = ip.split(":");
                if(ipAndPort.length == 2){
                    System.out.println(ipAndPort[0]+","+ipAndPort[1]);
                    Runnable runnable = ()->{
                       String success = successCSDN(url,ipAndPort[0], Integer.parseInt(ipAndPort[1].replaceAll("\\r","")));
                       if(success != null){
                           for(String art : arts){
                               successCSDN(art,ipAndPort[0], Integer.parseInt(ipAndPort[1].replaceAll("\\r","")));
                           }
                       }
                    };
                    threadPoolExecutor.submit(runnable);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("error");
        }
    }

    /**
     * 请求CSDN
     * @param url
     * @param vIp
     * @param vPort
     * @return
     */
    public static String successCSDN(String url,String vIp, int vPort){
        try{
            Random r = new Random();
            String[] ua = {"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.87 Safari/537.36 OPR/37.0.2178.32",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
                    "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 BIDUBrowser/8.3 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.277.400 QQBrowser/9.4.7658.400",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 UBrowser/5.6.12150.8 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36 SE 2.X MetaSr 1.0",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36 TheWorld 7",
                    "Mozilla/5.0 (Windows NT 6.1; W…) Gecko/20100101 Firefox/60.0"};
            int i = r.nextInt(14);
            Document doc = Jsoup.connect(url)
                    .timeout(3000)
                    .proxy(vIp, vPort)
                    .ignoreContentType(true)
                    .userAgent(ua[i])
                    .header("referer",url)//这个来源记得换..
                    .post();
            System.out.println(doc.title()+","+url);
            return doc.title();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }

    }


    /**
     * 获取虚拟IP
     * @param url
     * @return
     */
    public static  String[] viewIPPage(String url){
        try {
            HtmlPage page =  getHtmlPage( url);//登陆页面
            String[] ips = page.getBody().asText().split("\\n");
            return ips;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String[] viewIPPage2(String href) throws IOException {
        List<String> ipList = new ArrayList<>();
        WebClient webClient = getWebClient();
        for (int i = 1; i < 10; i++) {
            try {
                String url = href.replaceAll("_PAGE",String.valueOf(i));
                HtmlPage page =  webClient.getPage(url);
                DomNodeList<DomElement> trList =  page.getElementsByTagName("tr");
                for(DomElement dom : trList){
                    DomNodeList<HtmlElement> tdList = dom.getElementsByTagName("td");
                    if(tdList!=null && tdList.size()>3){
                        String ip = tdList.get(1).asText()+":"+tdList.get(2).asText();
                        ipList.add(ip);
                        System.out.println(ip);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        webClient.close();
        webClient = null;
        return ipList.toArray(new String[ipList.size()]);

    }

    /**
     * 获取csdn文章列表
     * @param url
     */
    public static void artical(String url) throws IOException {

        WebClient webClient = getWebClient();
        try {
            HtmlPage page =  webClient.getPage( url);
            DomNodeList<DomElement> pdivList =  page.getElementsByTagName("p");
            for(DomElement dom : pdivList){
                DomNodeList<HtmlElement> aDomList = dom.getElementsByTagName("a");
                if(aDomList != null && aDomList.size()>0){
                    for(HtmlElement aDom: aDomList){
                        String href = aDom.getAttribute("href");
                        if(href.contains("article")&&href.contains("details")){
                            artList.add(href);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        webClient.close();
        webClient = null;
    }

    public static HtmlPage getHtmlPage(String url) throws IOException {
        if(htmlPage == null){

            WebClient webClient=new WebClient(BrowserVersion.CHROME);

            //设置Ajax控制器
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //是否启用js
            webClient.getOptions().setActiveXNative(true);
            webClient.getOptions().setRedirectEnabled(true);
            //   webClient.getOptions().set
            // webClient.getOptions().setJavaScriptEnabled(true);
            //是否启用css
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            htmlPage = webClient.getPage(url);
        }
        return  htmlPage;
    }

    public static WebClient getWebClient() throws IOException {

        if(webClient == null){
             webClient=new WebClient(BrowserVersion.CHROME);

            //设置Ajax控制器
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //是否启用js
            webClient.getOptions().setActiveXNative(true);
            webClient.getOptions().setRedirectEnabled(true);
            //   webClient.getOptions().set
            // webClient.getOptions().setJavaScriptEnabled(true);
            //是否启用css
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        }
         return webClient;
    }
}

