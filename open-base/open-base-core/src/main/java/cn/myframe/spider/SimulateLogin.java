package cn.myframe.spider;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class SimulateLogin
{
    //访问的目标网址（CSDN）
    private static String TARGET_URL = "https://passport.csdn.net/account/login?from=http://www.csdn.net";

    public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException
    {
        // 模拟一个浏览器
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        // 设置webClient的相关参数
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        //设置ajax
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //设置支持js
        webClient.getOptions().setJavaScriptEnabled(true);
        //CSS渲染禁止
        webClient.getOptions().setCssEnabled(false);
        //超时时间
        webClient.getOptions().setTimeout(50000);
        //设置js抛出异常:false
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //允许重定向
        webClient.getOptions().setRedirectEnabled(true);
        //允许cookie
        webClient.getCookieManager().setCookiesEnabled(true);
        // 模拟浏览器打开一个目标网址
        HtmlPage page = webClient.getPage(TARGET_URL);
        /**等待js加载完全，CSDN这点 特别坑，js加载时间超长！！！！！！！ 后人切记不要用CSDN模拟登陆！！！！！！！**/
        webClient.waitForBackgroundJavaScript(10000*3);
        System.out.println(page.asXml());

        DomNodeList<DomNode> articleList = page.querySelectorAll(".text-tab");
        for(DomNode node :articleList){

            System.out.println(node.asText());
        }


        // proxy
        //webClient.getOptions().setProxyConfig(new ProxyConfig("IP", 80));

        // 根据form的名字获取页面表单，也可以通过索引来获取：page.getForms().get(0)
        HtmlForm form = (HtmlForm) page.getElementById("fm1");
        HtmlTextInput username = (HtmlTextInput) form.getInputByName("username");
        HtmlPasswordInput password = (HtmlPasswordInput) form.getInputByName("password");
        username.setValueAttribute("********");  //用户名
        password.setValueAttribute("********");  //密码
        HtmlButtonInput button  = (HtmlButtonInput) page.getByXPath("//input[contains(@class, 'logging')]").get(0);
//        ScriptResult result = page.executeJavaScript("javascript:document.getElementsByClassName('logging')[0].click()");
//        HtmlPage retPage = (HtmlPage) result.getNewPage();
        HtmlPage retPage = button.click();
        // 等待JS驱动dom完成获得还原后的网页
        webClient.waitForBackgroundJavaScript(1000);
        //输出跳转网页的地址
        System.out.println(retPage.getUrl().toString());
        //输出跳转网页的内容
        System.out.println(retPage.asXml());

        //获取cookie
        Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        Map<String, String> responseCookies = new HashMap<String, String>();
        for (Cookie c : cookies) {
            responseCookies.put(c.getName(), c.getValue());
            System.out.print(c.getName()+":"+c.getValue());
        }
        webClient.close();
        System.out.println("Success!");
    }
}
