package cn.myframe.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;


/**
 * @Author: ynz
 * @Date: 2019/1/17/017 11:17
 * @Version 1.0
 */
@Document(indexName = "nias12",type = "receiver3",refreshInterval="120s",replicas=1)
@Data
public class BusReceiverEntity {

    //主键
    private Long id;
    //姓名
    private String name;
    //区域
    private String regionCode;
    //地址
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart", fielddata=true)
    private String address;
    //地址英文名字
    private String enName;
    //家庭成员
    @Field(type = FieldType.Integer)
    private int memberFamily;
    //创建时间
    private Date createDate;

    public BusReceiverEntity(Long id,String name, String regionCode,String address){
        this.id = id;
        this.name = name;
        this.regionCode = regionCode;
        this.address = address;
    }

    public BusReceiverEntity(Long id,String name, String regionCode,String address,String enName,int memberFamily){
        this.id = id;
        this.name = name;
        this.regionCode = regionCode;
        this.address = address;
        this.enName = enName;
        this.memberFamily = memberFamily;
        this.createDate  = new Date();

    }

    public BusReceiverEntity(){

    }

}
