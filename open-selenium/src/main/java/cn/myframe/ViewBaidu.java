package cn.myframe;

import cn.myframe.utils.PageUtils;
import org.openqa.selenium.WebDriver;

/**
 * @Author: ynz
 * @Date: 2019/5/13/013 13:55
 * @Version 1.0
 */
public class ViewBaidu {

    public static void main(String[] args) throws InterruptedException {
        //开启个浏览器并且输入链接
        WebDriver driver = PageUtils.getChromeDriver("https://www.baidu.com/");
        //得到浏览器的标题
        System.out.println(driver.getTitle());
        Thread.sleep(5000);
        //关闭浏览器 下面是关闭所有标签页，还有一个代码是 driver.close();, 关闭当前标签页
        driver.quit();
    }
}
