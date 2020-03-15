package cn.myframe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * mycat不支持二维路由，仅支持单库多表或多库单表
 * mycat以逻辑表的形式屏蔽掉应用处理分库分表的复杂逻辑，遵守Mysql原生协议，跨语言，跨平台，有着更为通用的应用场景
 *
 *
 * 启动方法
 * 
 * @author  ynz
 * @email   ynz@myframe.cn
 * @version 创建时间：2018年6月25日 下午5:32:00
 */
@SpringBootApplication
@MapperScan("cn.myframe.dao")
public class MycatApplication {
	
	public static void main(String[] args){
        SpringApplication app = new SpringApplication(MycatApplication.class);
       // app.setWebEnvironment(false);
        app.run(args);
    }

}
