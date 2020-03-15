package cn.myframe.spider;

import cn.myframe.csdn.CheatingApp;
import com.alibaba.fastjson.JSON;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@EnableScheduling
public class SpiderCsdn {

    @Autowired
    CheatingApp cheatingApp;

    //@Value("${article.list:https://blog.csdn.net/cowbin2012/article/list/NumPage?}")
    private String articleUpdateTimetUrl = "https://blog.csdn.net/AUTHOR/article/list/NumPage?orderby=UpdateTime";

    private String articleViewCountUrl = "https://blog.csdn.net/AUTHOR/article/list/NumPage?orderby=ViewCount";


    @Value("${article.author:cowbin2012}")
    private String author;

    @Value("${article.page-size:5}")
    private int pageSize;

    static HtmlPage htmlPage = null;

    static WebClient webClient= null;

    static String[] arts = null;

    static String[] lessArts = null;

    static ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(200, 300, 5, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100000));

    static long page = 0;

    static AtomicLong atomicLong = new AtomicLong();

    static volatile boolean flag = true;

    @Value("${article.ip-flag:0}")
    private  int ipFlag ;


    static Set<String> successVIPSet = new HashSet<>();

    public static Set<String> artIds = new HashSet<>();


    //@Scheduled(cron = "0 0/1 * * * ?")
    @PostConstruct
    public void start() throws IOException, InterruptedException {
        List<String> artList = artical(2,1);
        arts = artList.toArray(new String[artList.size()]);
        artical(pageSize,0);

    }
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void flushArt(){
        artical(pageSize,0);
    }

    /**
     * 执行评论
     */
    /*@Scheduled(cron = "0 0/1 * * * ?")*/
    public void cheat(){
        cheatingApp.runCommentMachine();
    }


    @Scheduled(cron = "0 0/2 * * * ?")
    public void start2() throws IOException, InterruptedException {
        long time = System.currentTimeMillis();
        long l = atomicLong.get();
        int count = 0;
        if(arts != null){
            String[] vIps = null;
            if(count>10){
                successVIPSet.clear();
            }
            if(successVIPSet.size()>0){
                List<String> list = new ArrayList<String>();
                list.addAll(successVIPSet);
                vIps = list.toArray(new String[list.size()]);
                count ++;
            }else{
                if(ipFlag == 0){
                    vIps = viewIPPage("http://www.89ip.cn/tqdl.html?api=1&num=5000&port=&address=&isp=");
                }else{
                    vIps = viewIPPage2("https://www.xicidaili.com/nt/");
                }
            }

            if(vIps != null && arts.length>0){
                for(String ip : vIps){
                    concurrentViewCsdn( ip, arts[new Random().nextInt(arts.length)],true);
                }
            }

        }
       /* while(threadPoolExecutor.getActiveCount()!=0){
            Thread.sleep(2000);
        }
        log.error("请求数："+String.valueOf(atomicLong.get()-l)+"耗时："+(System.currentTimeMillis()-time));*/
    }


    public static void concurrentViewCsdn(String ip, String url,boolean flag){
        try{
            if(ip.contains(":")){
                String[] ipAndPort = ip.split(":");
                if(ipAndPort.length == 2){
                    Runnable runnable = ()->{
                        String success = successCSDN(url,ipAndPort[0], Integer.parseInt(ipAndPort[1].replaceAll("\\r","")));
                        if(flag){
                            if(success != null && lessArts!=null){
                                successVIPSet.add(ip);
                                int i = 0;
                                for(String art : lessArts){
                                    String reponse = successCSDN(art,ipAndPort[0], Integer.parseInt(ipAndPort[1].replaceAll("\\r","")));
                                    if(reponse!=null && reponse.contains("专业IT技术社区") && i++ >5){
                                        break;
                                    }
                                }
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
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0)",
                    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)",
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
                    .header("referer",url)
                    .post();
            log.error(doc.title()+","+url);
            atomicLong.incrementAndGet();
            return doc.title();
        }catch (Exception e){
          //  log.error(e.getMessage());
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
            HtmlPage page =  getHtmlPage( url);
            String[] ips = page.getBody().asText().split("\\n");
            return ips;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String[] viewIPPage2(String href) throws IOException {
        //"https://www.xicidaili.com/nt/"+page
        List<String> ipList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            href = href + i;
            WebClient webClient = getWebClient();
            try {
                HtmlPage page =  webClient.getPage(href);
                DomNodeList<DomElement> trList =  page.getElementsByTagName("tr");
                for(DomElement dom : trList){
                    DomNodeList<HtmlElement> tdList = dom.getElementsByTagName("td");
                    if(tdList!=null && tdList.size()>3){
                        String ip = tdList.get(1).asText()+":"+tdList.get(2).asText();
                        //   System.out.println(ip);
                        ipList.add(ip);
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            webClient.close();
            webClient = null;
        }

        return ipList.toArray(new String[ipList.size()]);

    }


    /**
     * 获取csdn文章列表
     */
    public  List<String> artical(int pageSize,int flag) {
        List<String> artList = new ArrayList<String>();
        List<String> lessArtList = new ArrayList<String>();
        String articleListUrl = (flag == 0 ? articleUpdateTimetUrl:articleViewCountUrl);
        WebClient webClient = null;
        try {
            int artSize = 0;
            for(int i=0;i<pageSize;i++){
                artSize = artList.size();
                webClient = getWebClient();
                HtmlPage page =  webClient.getPage(articleListUrl.replaceAll("NumPage",String.valueOf(i+1))
                        .replaceAll("AUTHOR",author));
               /* System.out.println(page.asXml());;*/
                DomNodeList<DomNode> articleList = page.querySelectorAll(".article-item-box");
                if(articleList!=null && articleList.size()>0){
                    for(DomNode domNode : articleList){
                        String articleid = ((HtmlDivision)domNode).getAttribute("data-articleid");
                        artIds.add(articleid);
                        DomNode numNode = domNode.querySelector(".num");
                        if(numNode != null
                                && StringUtils.isNotEmpty(numNode.asText())){
                            HtmlAnchor aNode = (HtmlAnchor)domNode.querySelector("a");
                            if(aNode != null){
                                String href = aNode.getAttribute("href");
                                if(href.contains("article")
                                        &&href.contains("details")&&href.contains(author)){
                                    artList.add(href);
                                    if(Integer.parseInt(numNode.asText())<10000){
                                        lessArtList.add(href);
                                    }
                                }
                            }
                        }
                    }
                }
                webClient.close();
                webClient = null;
                if(artSize == artList.size()){
                    break;
                }
            }
            if(lessArtList.size()>0){
                lessArts = lessArtList.toArray(new String[lessArtList.size()]);
            }
        } catch (Exception e) {
            webClient.close();
            webClient = null;
            e.printStackTrace();
        }
        return artList;
    }

    public static HtmlPage getHtmlPage(String url) throws IOException {
        if(htmlPage == null){

            WebClient webClient=new WebClient(BrowserVersion.CHROME);

            //设置Ajax控制器
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            //是否启用js
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setActiveXNative(true);
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
            webClient.getOptions().setRedirectEnabled(true);
            webClient.getOptions().setActiveXNative(true);
            //   webClient.getOptions().set
            // webClient.getOptions().setJavaScriptEnabled(true);
            //是否启用css
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

        }
        return webClient;
    }

    public void changeAuthor(String author){
        flag = true;
        this.author = author;
    }

}
