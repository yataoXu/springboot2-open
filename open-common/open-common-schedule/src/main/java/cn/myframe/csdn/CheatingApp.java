package cn.myframe.csdn;

import cn.myframe.csdn.machine.CommentMachine;
import cn.myframe.csdn.machine.VoteMachine;
import cn.myframe.properties.CommentProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huanghuapeng create at 2019/5/5 19:33
 * @version 1.0.0
 */
@Component
public class CheatingApp {

    @Autowired
    CommentMachine commentMachine;


    @Autowired
    CommentProperties commentProperties;

    public static List<String> cookieList = new ArrayList<>();


    @PostConstruct
    public void init(){
        cookieList = commentProperties.getCookieValues();
    }
   // public static final String COOK_VALUE = "uuid_tt_dd=10_19926310520-1546525573836-354072; UN=cowbin2012; smidV2=20190106090303b453f10647ac2a5bd61540fd55880f6700ec31a6b70e64650; __yadk_uid=iPN6FFSr7GaiVATfyiKCO80xvdnXhkg0; ADHOC_MEMBERSHIP_CLIENT_ID1.0=c0f26298-8b5c-e364-cff0-6768f5eadeeb; UM_distinctid=168f951c453739-02db82f77ed812-58422116-144000-168f951c454416; Hm_ct_6bcd52f51e9b3dce32bec4a3997715ac=5744*1*cowbin2012!1788*1*PC_VC!6525*1*10_19926310520-1546525573836-354072; UserName=cowbin2012; UserInfo=dd947ddfdf394ad1be875034ad9e5291; UserToken=dd947ddfdf394ad1be875034ad9e5291; UserNick=cowbin2012; AU=DC4; BT=1556961891047; dc_session_id=10_1557062429912.942527; Hm_lvt_6bcd52f51e9b3dce32bec4a3997715ac=1556981135,1556981768,1557062945,1557062956; dc_tos=pr1aoc; Hm_lpvt_6bcd52f51e9b3dce32bec4a3997715ac=1557064525";



    public static void main(String[] args) {
     //   new Thread(CheatingApp::runVoteMachine).start();
     //   new Thread(CheatingApp::runCommentMachine).start();
    }


    /**
     * 点赞机
     */
    private static void runVoteMachine() {
        int page = 4;
        String prefix = "https://blog.csdn.net/cowbin2012/article/category/1394262/";
        VoteMachine.startup(prefix, page);
    }

    /**
     * 留言机
     */
    public  void runCommentMachine() {
        commentMachine.startup();
    }
}
