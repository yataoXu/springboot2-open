package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * https://www.hutool.cn/docs/#/
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
public class HuApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(HuApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
