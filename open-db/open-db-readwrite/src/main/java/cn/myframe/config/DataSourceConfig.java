package cn.myframe.config;

import cn.myframe.enums.DBTypeEnum;
import cn.myframe.util.MyRoutingDataSource;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix ="spring.master.datasource")
    public DataSource masterDataSource() {
        //使用DruidDataSource
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix ="spring.slave1.datasource")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    @ConfigurationProperties(prefix ="spring.slave2.datasource")
    public DataSource slave2DataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slave1DataSource") DataSource slave1DataSource,
                                          @Qualifier("slave2DataSource") DataSource slave2DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        targetDataSources.put(DBTypeEnum.SLAVE1, slave1DataSource);
        targetDataSources.put(DBTypeEnum.SLAVE2, slave2DataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }
}
