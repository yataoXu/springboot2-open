package cn.myframe;

import cn.myframe.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/5/9/009 17:19
 * @Version 1.0
 */
public class TestHelloWold {


   /* @Test*/
    public  void main() {
        //开启个浏览器并且输入链接
        WebDriver driver = PageUtils.getChromeDriver("https://www.baidu.com/");

        //向input输入值
        PageUtils.inputStrByJS(driver, "kw", "月之暗面 博客园");

        //3、得到百度一下的标签
        WebElement submitElement = driver.findElement(By.cssSelector("input#su"));

        //4、点击百度一下
        PageUtils.scrollToElementAndClick(submitElement, driver);

        //休息3秒，加载数据
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //5、首先找到 id 为 content_left 的 div 下面的所有 div
        List<WebElement> divElements = driver.findElements(By.cssSelector("div#content_left div"));
        //6、找到搜索的第一个链接
        WebElement aElement = divElements.get(0).findElement(By.cssSelector("div.f13 a[href]"));

        //7、点击该链接
        PageUtils.scrollToElementAndClick(aElement, driver);
    }

  /*  @Test*/
    public  void main2() {
        //开启个浏览器并且输入链接
        WebDriver driver = PageUtils.getChromeDriver("https://www.baidu.com/");

        //向input输入值
        PageUtils.inputStrByJS(driver, "kw", "月之暗面 博客园");

        //3、得到百度一下的标签
        WebElement submitElement = driver.findElement(By.cssSelector("input#su"));

        //4、点击百度一下
        PageUtils.scrollToElementAndClick(submitElement, driver);

        //休息3秒，加载数据
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //5、首先找到 id 为 content_left 的 div 下面的所有 div
        List<WebElement> divElements = driver.findElements(By.cssSelector("div#content_left div"));
        //6、找到搜索的第一个链接
        WebElement aElement = divElements.get(0).findElement(By.cssSelector("div.f13 a[href]"));

        //7、点击该链接
        PageUtils.scrollToElementAndClick(aElement, driver);
    }


    @Test
    public void Untitled() {
        WebDriver driver = PageUtils.getChromeDriver("https://passport.csdn.net/login");
      //  driver.get("https://passport.csdn.net/login");
        driver.manage().window().setSize(new Dimension(1456, 876));
        driver.findElement(By.linkText("账号登录")).click();
        driver.findElement(By.id("all")).click();
        driver.findElement(By.id("all")).click();
        driver.findElement(By.id("all")).sendKeys("13422637324");
        driver.findElement(By.id("password-number")).click();
        driver.findElement(By.id("password-number")).sendKeys("qq2781969");
        driver.findElement(By.cssSelector(".btn")).click();
        driver.findElement(By.cssSelector(".title-blog")).click();
        driver.findElement(By.linkText("按更新时间")).click();
        driver.findElement(By.linkText("Rax 是 Redis 内部比较特殊的一个数据结构，它是一个有序字典树 (基数树 Radix Tree)，按照 key 的字典序排列，支持快速地定位、插入和删除操作。 Redis 五大基础数据结构里面，能作为字典使用的有 hash 和 zset。hash 不具 备排序功能，zset 则是按照 sc...")).click();
     //   driver.switchTo().window("vars.get("win9439").toString()");
        driver.findElement(By.cssSelector(".article-header-box")).click();
        driver.findElement(By.cssSelector(".btn-close-adddd")).click();
        driver.findElement(By.cssSelector(".btn-like > p")).click();
        /*{
            WebElement element = driver.findElement(By.cssSelector(".btn-like > p"));
            Action builder = new Actions(driver);
            builder.moveToElement(element).perform();
        }
        {
            WebElement element = driver.findElement(By.tagName("body"));
            Action builder = new Actions(driver);
            builder.moveToElement(element, 0, 0).perform();
        }*/
        driver.findElement(By.cssSelector(".title-article")).click();
        driver.findElement(By.cssSelector(".icon > path")).click();
        driver.findElement(By.cssSelector("p:nth-child(6)")).click();
    }

}
