package cn.myframe.csdn.handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 获取栏目下面的文章id
 *
 * @author huanghuapeng create at 2019/5/5 17:07
 * @version 1.0.0
 */
public class ArtIdHandlerImpl extends AbsRequestHandler<Object, List<String>> {

    @Override
    public List<String> parse(String html, String reqUrl) {
        Document doc = Jsoup.parse(html);
        Elements select = doc.select(".article-item-box.csdn-tracking-statistics");

        Set<String> set = new HashSet<>();
        for (Element e : select) {
            Elements a = e.select("a");
            String url = a.attr("href");
            if (url.contains("cowbin2012")) {
                set.add(url);
            }
        }

        return new ArrayList<>(set);
    }
}
