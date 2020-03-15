package cn.myframe.spider;

import cn.myframe.demo.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ynz
 * @Date: 2019/12/6/006 15:04
 * @Version 1.0
 */
public class JsoupCsdn {


    static Set<String> vIpSet = new HashSet<>();


    static String CSDN_URL = "https://blog.csdn.net/cowbin2012/article/list/";

    static String V_IP = "https://www.xicidaili.com/nn/";

    static Map<String,AtomicInteger> ART_MAP = new HashMap<>();

    static Map<String,Integer> VIP_MAP = new HashMap<>();


  //  static Set<String> artSet = new HashSet<>();

    static HashMap<String, String> map = new HashMap<String,String>();
    static{
        map.put("TINGYUN_DATA", "%7B%22id%22%3A%22-sf2Cni530g%23HL5wvli0FZI%22%2C%22n%22%3A%22WebAction%2FCI%2Farticle%252Fdetails%22%2C%22tid%22%3A%2212cb78391002478%22%2C%22q%22%3A0%2C%22a%22%3A132%7D");

        VIP_MAP.put("125.88.190.1",3128);
        VIP_MAP.put("123.59.211.215",3128);
        VIP_MAP.put("140.143.48.49",1080);
        VIP_MAP.put("120.77.206.107",3128);
        VIP_MAP.put("183.154.48.79",9999);
        VIP_MAP.put("27.152.24.157",9999);
        VIP_MAP.put("183.154.48.79",9999);


    }

    public static void main(String[] args) {
       /* String url = "https://blog.csdn.net/cowbin2012/article/list/4?";
       try {
            Connection conn = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).
                    userAgent("Mozilla");
            Document doc = conn.timeout(8000).cookies(map).get();
            //Document doc = Jsoup.connect(url).get().ignoreContentType(true).ignoreHttpErrors(true).userAgent("Mozilla");;
            System.out.println(doc);
            findArtMap(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        findAllArt();
       /* new Thread(()->{
              requestArt();
         }).start();*/
        for(Map.Entry<String,Integer> entry : VIP_MAP.entrySet()){
            new Thread(()->{
                requestArt(entry.getKey(),entry.getValue());
            }).start();
        }
        //findVip();
    }

    public static void findAllArt(){
        for (int i = 1; i <= 10; i++) {
            try{
                System.out.println(String.format("请求第%s页数据.........",String.valueOf(i)));
                String url = CSDN_URL + i + "?";
                Connection conn = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true).
                        userAgent("Mozilla");
                try {
                    Document doc = conn.timeout(8000).cookies(map).get();
                    findArtMap(doc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread.sleep(3_000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void requestArt(){
        for(Map.Entry<String,AtomicInteger> entry : ART_MAP.entrySet()){
            try{
                if(entry.getValue().get() > 0){
                    Connection conn = Jsoup.connect(entry.getKey()).ignoreContentType(true).ignoreHttpErrors(true).
                            userAgent("Mozilla");
                    Document doc = conn.timeout(8000).cookies(map).get();
                    entry.getValue().decrementAndGet();
                    Thread.sleep(4_000);
                }
                System.out.println(String.format("请求连接:%s",entry.getKey()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void requestArt(String ip ,int port){
        for(Map.Entry<String,AtomicInteger> entry : ART_MAP.entrySet()){
            try{
                if(entry.getValue().get() > 0){
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                    Connection conn = Jsoup.connect(entry.getKey()).ignoreContentType(true).ignoreHttpErrors(true).
                            userAgent("Mozilla");
                    Document doc = conn.proxy(proxy).timeout(3000).cookies(map).get();
                    entry.getValue().decrementAndGet();
                    Thread.sleep(3_000);
                }
                System.out.println(String.format("请求连接:%s",entry.getKey()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    public static void findArtMap(Document doc){
        Elements divElements = doc.select("div.article-item-box");
        for(Element link : divElements){
            Element element = link.select("span.num").first();
            String numStr = element.text();
            if(Integer.parseInt(numStr) < 10000){
                Element contentEle = link.select("h4 > a").first();
                String artUrl = contentEle.attr("href");
                if(artUrl!=null&&artUrl.startsWith("http")){
                    ART_MAP.put(artUrl,new AtomicInteger(10000-Integer.parseInt(numStr)));
                }
            }
            System.out.println(String.format("阅读量小于10000的文章数:%s",ART_MAP.size()));
            //System.out.println(element.text());
        }
    }

    public static void findVip(){
        for (int i = 1; i <= 5; i++) {
            Connection conn = Jsoup.connect(V_IP+i).ignoreContentType(true).ignoreHttpErrors(true).
                    userAgent("Mozilla");
            try {
                Document doc = conn.timeout(8000).get();
                Elements trElements = doc.select("tr.odd");
                for(Element trEle : trElements){
                    Elements tdEles = trEle.select("td");
                    if(tdEles.size() > 4){
                        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tdEles.get(1).text(), Integer.parseInt(tdEles.get(2).text())));
                        Jsoup.connect("https://blog.csdn.net/localhost01/article/details/83422902").ignoreContentType(true).ignoreHttpErrors(true).
                                userAgent("Mozilla");
                        conn.proxy(proxy);
                        try{
                            conn.timeout(500).get();
                            vIpSet.add(tdEles.get(1).text() +":"+tdEles.get(2).text());
                        }catch (Exception e){

                        }
                    }

                    // System.out.println(ipNode.outerHtml());
                }
                System.out.println("vip:"+vIpSet.size());
                for(String ip : vIpSet){
                    System.out.println(ip);
                }
                //System.out.println(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
