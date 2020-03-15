package cn.myframe.config;

import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: ynz
 * @Date: 2019/4/22/022 17:18
 * @Version 1.0
 */
/*@Configuration*/
public class SolrCloudConfig {

    @Value("${spring.data.solr.zk-host}")
    private String zkHost;

    //@Bean
    public CloudSolrClient solrClient() {
        List<String> zkHosts = new ArrayList<String>();
        zkHosts.add("10.10.2.137:2181");
        zkHosts.add("10.10.2.138:2182");
        zkHosts.add("10.10.2.139:2183");
        Optional<String> zkChroot = Optional.of("/");
        //builder的构造函数需要一个List和一个Optional
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder(zkHosts, zkChroot);
        CloudSolrClient solrClient = builder.build();
        return solrClient;
    }
}
