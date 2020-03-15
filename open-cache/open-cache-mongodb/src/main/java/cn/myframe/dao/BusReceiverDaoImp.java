package cn.myframe.dao;

import cn.myframe.entity.BusReceiverEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
public class BusReceiverDaoImp implements BaseDao<BusReceiverEntity> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insert(BusReceiverEntity entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public void batchInsert(List<BusReceiverEntity> list) {
        mongoTemplate.insert(list, BusReceiverEntity.class);
    }

    @Override
    public BusReceiverEntity findById(Serializable id) {
        Query query = new Query(Criteria.where("id").is(id));
        BusReceiverEntity ent = mongoTemplate.findOne(query, BusReceiverEntity.class);
        return ent;
    }

    /**
     * 普通分页,逐行扫描,数据多时查询慢
     * @param limit
     * @param pageNo
     * @return
     */
    public List<BusReceiverEntity> findPage(int limit,int pageNo){
        Query query = new Query()
                .skip((pageNo-1)*limit)
                .limit(limit);
        List<BusReceiverEntity> list = mongoTemplate.find(query,BusReceiverEntity.class);
        return list;
    }

    /**
     * 改进分页查询
     * 通过保留上一次查询分布最后一条数据的Id
     * @param limit
     * @param pageNo
     * @param preId
     * @return
     */
    public List<BusReceiverEntity> findPage2(int limit,int pageNo,Serializable preId){
        Query query = new Query(Criteria.where("id").gt(preId));
        query.with(new Sort(Sort.Direction.ASC, "_id"));//排序
        query.limit(limit);
      //  query.add
        List<BusReceiverEntity> list = mongoTemplate.find(query,BusReceiverEntity.class);
        return list;
    }


    @Override
    public void update(BusReceiverEntity entity) {
        Query query = new Query(Criteria.where("_id").is(entity.getId()));
        Update update = new Update()
                .set("name", entity.getName());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query, update, BusReceiverEntity.class);
    }

    @Override
    public void delete(Serializable id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, BusReceiverEntity.class);
    }
}
