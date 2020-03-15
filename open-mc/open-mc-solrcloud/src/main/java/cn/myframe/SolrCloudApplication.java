package cn.myframe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@rojao.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */

/**
 * https://www.oschina.net/translate/spring-data-solr-tutorial-sorting?cmp
 */
@SpringBootApplication
/*@EnableAutoConfiguration*/
public class SolrCloudApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(SolrCloudApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
