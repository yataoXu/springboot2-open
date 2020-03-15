package cn.myframe.service;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.repository.ReceiverRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: ynz
 * @Date: 2019/1/17/017 11:26
 * @Version 1.0
 */
@Service
public class BusReceiverServiceImp implements  BusReceiverService{

    @Autowired
    ReceiverRepository receiverRepository;

    @Override
    public List<BusReceiverEntity> queryPage() {
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("");
        Pageable pageable =  PageRequest.of(0,10);
        Page<BusReceiverEntity> page =  receiverRepository.search(queryBuilder,pageable);
        return page.getContent();
    }

    @Override
    public BusReceiverEntity queryObject(Long id) {
        return null;
    }

    @Override
    public void save(BusReceiverEntity t) {
        receiverRepository.save(t);
    }

    @Override
    public void saveBatch(List<BusReceiverEntity> list) {
        receiverRepository.saveAll(list);
    }

    @Override
    public List<BusReceiverEntity> queryList(Map<String, Object> map) {
        return null;
    }

    @Override
    public void update(BusReceiverEntity t) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteAll() {
        receiverRepository.deleteAll();
    }

    @Override
    public List<BusReceiverEntity> queryByName(String name) {
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(name);
        Pageable pageable =  PageRequest.of(0,10000);
        Page<BusReceiverEntity> page =  /*receiverRepository.search(queryBuilder,pageable)*/null;

        BoolQueryBuilder qb=QueryBuilders. boolQuery();
        qb.should(QueryBuilders.matchQuery("name",name));
        page =  receiverRepository.search(qb,pageable);
        return page.getContent();
    }


}
