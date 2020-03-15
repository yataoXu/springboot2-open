package cn.myframe.csdn.machine;

import cn.myframe.csdn.handler.AbsRequestHandler;
import cn.myframe.csdn.handler.ArtIdHandlerImpl;
import cn.myframe.csdn.handler.VoteHandlerImpl;


import java.util.ArrayList;
import java.util.List;

/**
 * csdn点赞机
 *
 * @author huanghuapeng create at 2019/5/5 17:01
 * @version 1.0.0
 */
public class VoteMachine {

    /**
     * 启动点赞机
     *
     * @param categoryUrl 栏目url,如 JAVA对应url为:https://blog.csdn.net/cowbin2012/article/category/1394262
     * @param page        该栏目下的页码
     */
    public static void startup(String categoryUrl, int page) {
        if (!categoryUrl.endsWith("/")) {
            categoryUrl += "/";
        }

        List<String> idList = getArtUrlList(categoryUrl, page);
        for (String each : idList) {
            vote(each);
        }
    }

    /**
     * List
     *
     * @param urlPrefix url前缀(注意/结尾): https://blog.csdn.net/cowbin2012/article/category/8915634/
     * @param page      页数
     * @return list
     */
    private static List<String> getArtUrlList(String urlPrefix, int page) {
        AbsRequestHandler<Object, List<String>> artImpl = new ArtIdHandlerImpl();

        List<String> ids = new ArrayList<>();
        for (int index = 1; index <= page; index++) {
            List<String> urls = artImpl.get(urlPrefix + index);
            for (String each : urls) {
                int i = each.lastIndexOf("/");
                ids.add(each.substring(i + 1));
            }
        }
        return ids;
    }

    /**
     * 点赞,传入文章id
     *
     * @param artId 文章id
     */
    private static void vote(String artId) {
        String url = "https://blog.csdn.net/cowbin2012/phoenix/article/digg?ArticleId=" + artId;
        VoteHandlerImpl impl = new VoteHandlerImpl();
        impl.get(url);
    }
}
