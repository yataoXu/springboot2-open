package cn.myframe.service;

import cn.myframe.entity.BusReceiverEntity;
import cn.myframe.utils.HBaseColumn;
import cn.myframe.utils.HQuery;
import cn.myframe.utils.HbaseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.TableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/1/29/029 9:47
 * @Version 1.0
 */
@Service
@Slf4j
public class BusReceiverServiceImp implements BusReceiverService {

    @Autowired
    private HbaseUtil hbaseUtil;

    /**
     * 创建表
     */
   @PostConstruct
    public  void createTable(){
       String[] familys = {"base","extends"};
       TableName tableName = TableName.valueOf("nias:bus_receiver");
       hbaseUtil.createTable("nias:bus_receiver",familys);
    }

    @Override
    public void save(BusReceiverEntity busReceiverEntity) {
        HQuery hQuery = new HQuery("nias:bus_receiver",String.valueOf(busReceiverEntity.getId()));
        hQuery.getColumns().add(new HBaseColumn("base","name",busReceiverEntity.getName(),busReceiverEntity.getId()));
        hQuery.getColumns().add(new HBaseColumn("base","regionCode",busReceiverEntity.getRegionCode(),busReceiverEntity.getId()));
        hQuery.getColumns().add(new HBaseColumn("extends","address",busReceiverEntity.getAddress(),busReceiverEntity.getId()));
        hQuery.getColumns().add(new HBaseColumn("extends","memberFamily",String.valueOf(busReceiverEntity.getMemberFamily()),busReceiverEntity.getId()));
        hQuery.getColumns().add(new HBaseColumn("extends","enName",String.valueOf(busReceiverEntity.getEnName()),busReceiverEntity.getId()));
        hbaseUtil.bufferInsert(hQuery);
    }

    public void batchSave(List<BusReceiverEntity> list){
        HQuery hQuery = new HQuery("nias:bus_receiver");
        for(BusReceiverEntity busReceiverEntity:list){
            hQuery.getColumns().add(new HBaseColumn("base","name",busReceiverEntity.getName(),busReceiverEntity.getId()));
            hQuery.getColumns().add(new HBaseColumn("base","regionCode",busReceiverEntity.getRegionCode(),busReceiverEntity.getId()));
            hQuery.getColumns().add(new HBaseColumn("extends","address",busReceiverEntity.getAddress(),busReceiverEntity.getId()));
            hQuery.getColumns().add(new HBaseColumn("extends","memberFamily",String.valueOf(busReceiverEntity.getMemberFamily()),busReceiverEntity.getId()));
            hQuery.getColumns().add(new HBaseColumn("extends","enName",String.valueOf(busReceiverEntity.getEnName()),busReceiverEntity.getId()));
        }
        hbaseUtil.bufferInsert(hQuery);
    }

    @Override
    public BusReceiverEntity queryByRowId(Serializable id) {
        HQuery hQuery = new HQuery("nias:bus_receiver",id);
        BusReceiverEntity busReceiverEntity = hbaseUtil.get(hQuery,BusReceiverEntity.class);
        return busReceiverEntity;
    }

    /**
     * 删除表
     * @param table
     */
    public void deleteAll(String table){
        hbaseUtil.deleteAll(table);
    }


}
