package cn.myframe.csdn.machine;

import cn.myframe.csdn.handler.CommentHandlerImpl;
import cn.myframe.spider.SpiderCsdn;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * csdn点赞机
 *
 * @author huanghuapeng create at 2019/5/5 17:01
 * @version 1.0.0
 */
@Component
public class CommentMachine {

    @Value("${comment.author:cowbin2012}")
    private String author;

    @Value("#{'${comment.authors:cowbin2012}'.split(',')}")
    private List<String> authors;

    String articleUrls = "https://blog.csdn.net/AUTHOR/phoenix/comment/submit?id=ARTID";

    private String articleUpdateTimetUrl = "https://blog.csdn.net/AUTHOR/article/list/NumPage?orderby=UpdateTime";

    volatile Set<String> artIds = new HashSet<>();

    private static String[] commentArr = {
            "啊,我好喜欢这篇文章",
            "学习java,这个博客[https://blog.csdn.net/cowbin2012]不错",
            "所有博文看一遍必成java大神[https://blog.csdn.net/cowbin2012]",
            "java太容易,看这博客[https://blog.csdn.net/cowbin2012]",
            "妈妈说学习java不难只需要一博客[https://blog.csdn.net/cowbin2012]",
            "太谢谢你了博客[https://blog.csdn.net/cowbin2012]",
            "博客有用[https://blog.csdn.net/cowbin2012]",
            "什么知道点都[https://blog.csdn.net/cowbin2012]",
            "这可以有",
            "收藏","66666","求带飞","这里说得更好[https://blog.csdn.net/cowbin2012]",
            "这里说得更全[https://blog.csdn.net/cowbin2012]",
            "学习java的进[https://blog.csdn.net/cowbin2012]",
            "还行","学习了","为什么","路过","再路过","再次路过","又一次路过",
            "找到你了","非常好","很好","nice","thanks","[https://blog.csdn.net/cowbin2012]这个不错",
            "一起进步","我想看完所有","再一次阅读","好好学习","求关注","关注","点赞","点赞+1","点赞+2","点赞+3","点赞+4","点赞+5",
            "点赞+666","我想说有用","路过学习","我想说还好吧","我想说一般","能用","说得好","说得一般","过得去","好吧","还不错",
            "再次学习","求学习资料","能转载不","可不可以转载","转载了",
            "大神","求带飞","带飞","这博客不错","共同进步","天天学习","努力学习","非常好","详细","竟然看懂了","懂了",
            "真的懂了","头头是道","果然是大神","大神是如何练成的","看到这博文真幸运","终于找到有用的","是你就是你找到了",
            "您说的都是对的","学java不，上[https://blog.csdn.net/cowbin2012]","不是学java的也来看看吧[https://blog.csdn.net/cowbin2012]",
            "好牛掰,膜拜,献膝盖,求带飞",
            "大神,可以收我为徒吗?",
            "你还是那么厉害呀,那么多年没见你",
            "不错","这文章厉害了","通透","终于明白了","说明真的不错",
            "专注JAVA的博客[https://blog.csdn.net/cowbin2012]"
    };

    @PostConstruct
    public void init(){
        flushArt();
    }

    int count = 1;
    int size = 0;
    boolean flag = true;
    @Scheduled(cron = "0 0/10 * * * ?")
    public void flushArt(){
        if(authors.size()>0){
            author = authors.get(size);
            artIds.clear();
            String articleListUrl = articleUpdateTimetUrl.replaceAll("NumPage",String.valueOf(count))
                    .replaceAll("AUTHOR",author);
            Set<String> artSet = articalId( articleListUrl);
            if(artSet.size()>0) {
                artIds.addAll(articalId(articleListUrl));
                count++;
            }else{
                size ++;
                if(size == authors.size()){
                    size = 0;
                }
            }
        }

    }


    /**
     * 启动留言机
     */
    public  void startup() {
        author = authors.get(size);
        Set<String> artSet = new HashSet<>();
        artSet.addAll(artIds);

        // 文章url
        CommentHandlerImpl impl = new CommentHandlerImpl();
        for(String articleId : artSet){
            Map<String, String> map = new HashMap<>(1);
            String articleUrl = articleUrls.replaceAll("AUTHOR",author).replaceAll("ARTID",articleId);
            String comment = commentArr[new Random().nextInt(commentArr.length)];
            if(comment.length()<10){
                comment = comment + ","+commentArr[new Random().nextInt(commentArr.length)];
            }
            map.put("content", comment);
            // 发送消息
            impl.post(articleUrl, map);
            try {
                int sleepSecond = 30+new Random().nextInt(5);
                Thread.sleep(sleepSecond * 1000);
            } catch (Exception e) {
                //
            }
        }

    }

    /**
     * 获取数组里面的随机元素
     *
     * @param arr string数组
     * @return String
     */
    private  String getRandomElement(String[] arr) {
        if (null == arr || arr.length == 0) {
            return "";
        }

        int len = arr.length;
        int index = (int) (Math.random() * len);

        return arr[index];
    }

    /**
     * 获取csdn文章列表
     */
    public Set<String> articalId(String articleListUrl) {
        Set<String> artIds = new HashSet<>();
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
            Document doc = Jsoup.connect(articleListUrl)
                    .timeout(3000)
                    //    .proxy(vIp, vPort)
                    .ignoreContentType(true)
                    .userAgent(ua[i])
                    .header("referer",articleListUrl)
                    .post();
            Elements select =  doc.select(".article-item-box.csdn-tracking-statistics");
            if(select != null){
                for (Element e : select) {
                    artIds.add(e.attr("data-articleid"));
                }
            }
        }catch (Exception e){
        }
        return artIds;
    }






}
