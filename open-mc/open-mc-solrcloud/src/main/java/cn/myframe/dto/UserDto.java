package cn.myframe.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Dynamic;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2019/4/22/022 18:02
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@SolrDocument(solrCoreName = "demo")
public class UserDto {

    @Field("id")
    private String userId;

    @Field("address")
    private String address;

    @Dynamic //动态域
    @Field("item_spec_*")
    private Map<String,String> specMap;//最好将范型加上
}
