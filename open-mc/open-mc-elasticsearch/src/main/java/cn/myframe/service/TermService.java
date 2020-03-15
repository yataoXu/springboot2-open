package cn.myframe.service;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.repository.ReceiverRepository;
import com.alibaba.fastjson.JSON;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @Author: ynz
 * @Date: 2019/1/17/017 13:09
 * @Version 1.0
 */
public class TermService {

    @Autowired
    ReceiverRepository receiverRepository;

    /**
     * 关键字查询，对查询字段不进行分词
     *
     *  QueryBuilders.termQuery("${fieldName}.keyword", "${fieldValue}");
     *
     */
    public void termSearch(){

        //查询不到数据，因为"蓬晨涛"保存是会分词为"蓬" "晨" "涛"，而term查询不进行分词
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name","蓬晨涛");
        //查询出所有包含"涛"的数据
        queryBuilder = QueryBuilders.termQuery("name","涛");
        //当address的type为keyword时，能查询到数据，因为address保存时不分词
        queryBuilder = QueryBuilders.termQuery("address","台湾省乐清广场861号");
        printData(queryBuilder);
    }

    /**
     * 一次匹配多个值
     * QueryBuilders.termsQuery("${fieldName}.keyword", "${fieldValues}");
     */
    public void termsSearch(){

        //查询出匹配"湖北省微山湖街836号" 或者"甘肃省燕儿岛路825号" 的数据
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("address","湖北省微山湖街836号","甘肃省燕儿岛路825号");
        printData(queryBuilder);
    }



    public void printData(QueryBuilder queryBuilder){
        Pageable pageable =  PageRequest.of(0,100);
        Page<BusReceiverEntity> page =  receiverRepository.search(queryBuilder,pageable);
        page.getContent().forEach(entity->{
            System.out.println(JSON.toJSONString(entity));
        });
        System.out.println( page.getContent().size());
    }
}
