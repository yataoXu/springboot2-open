package cn.myframe;

import cn.myframe.utils.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import java.util.Random;

/**
 * @Author: ynz
 * @Date: 2019/5/10/010 9:33
 * @Version 1.0
 */
public class NiasTest {

    @Test
    public void Untitled() throws InterruptedException {
        WebDriver driver = PageUtils.getChromeDriver("http://10.10.1.217:8078/nias/index.html");
        //  driver.get("https://passport.csdn.net/login");
       // driver.manage().window().setSize(new Dimension(1920, 1080));
        driver.manage().window().maximize();
      //  driver.manage().window().setSize(new Dimension(727, 676));
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("Rojao@123");
      /*  driver.findElement(By.id("kaptcha")).click();
        driver.findElement(By.id("kaptcha")).sendKeys("2");*/
        Thread.sleep(3000);
        driver.findElement(By.id("to-recover")).click();
        driver.navigate().refresh();
        Thread.sleep(1000);
        driver.findElement(By.linkText("广告管理")).click();
        {
            WebElement element = driver.findElement(By.linkText("广告管理"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        driver.findElement(By.linkText("广告素材库")).click();
        driver.findElement(By.id("tabs")).click();
        driver.findElement(By.cssSelector(".line-wrap")).click();
        driver.switchTo().frame(0);
        driver.findElement(By.linkText("新增")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".form-group:nth-child(2)"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }

        driver.findElement(By.name("materialLibName")).click();
        int i = new Random().nextInt(1000000000);
        driver.findElement(By.name("materialLibName")).sendKeys("S"+i);
        driver.findElement(By.linkText("关联")).click();
        {
            WebElement element = driver.findElement(By.cssSelector(".modal-backdrop"));
            Actions builder = new Actions(driver);
            builder.doubleClick(element).perform();
        }
        Thread.sleep(500);
        driver.findElement(By.cssSelector("#customId_0 > td:nth-child(2)")).click();
        driver.findElement(By.cssSelector(".bootstrap-dialog-footer-buttons > button")).click();
        Thread.sleep(1000);
        driver.findElement(By.name("materialLibDir")).click();
        driver.findElement(By.name("materialLibDir")).sendKeys("ere");
        driver.findElement(By.name("materialLibDesc")).click();
        driver.findElement(By.name("materialLibDesc")).sendKeys("ere");
        Thread.sleep(3000);
        driver.findElement(By.id("validateBtn")).click();
        Thread.sleep(1000);
        driver.switchTo().defaultContent();
   //     driver.switchTo().frame(1);
      //  assertThat(driver.switchTo().alert().getText(), is("操作成功"));
//        System.out.println(driver.switchTo().alert().getText());
   //     driver.switchTo().defaultContent();
        driver.findElement(By.linkText("确定")).click();

       // throw new NullPointerException();

    }
}