package cn.myframe.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @Author: ynz
 * @Date: 2018/12/30/030 13:53
 */
@RestController
@Slf4j
public class TopicController {

    @Autowired
    private AdminClient adminClient;

    @ApiOperation(value = "创建topic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicName", value = "topic名称",
                    defaultValue = "first_top", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "partitions", value = "分区数", defaultValue = "4",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "replicationFactor", value = "副本数", defaultValue = "1",
                    required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/createTopic")
    public String createTopic(String topicName,int partitions,int replicationFactor){
        adminClient.createTopics(Arrays.asList(new NewTopic(topicName,partitions,(short)replicationFactor)));
        return "create success";
    }

    @ApiOperation(value = "查看所有的topic")
    @GetMapping("/findAllTopic")
    public String findAllTopic() throws ExecutionException, InterruptedException {
        ListTopicsResult result = adminClient.listTopics();
        Collection<TopicListing> list = result.listings().get();
        List<String> resultList = new ArrayList<>();
        for(TopicListing topicListing : list){
            resultList.add(topicListing.name());
        }
        return JSON.toJSONString(resultList);
    }

    @ApiOperation(value = "查看topic详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicName", value = "topic名称",defaultValue = "first_top",
                    required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/info")
    public String topicInfo(String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topicName));
        Map<String,String> resultMap = new HashMap<>();
        result.all().get().forEach((k,v)->{
            log.info("k: "+k+" ,v: "+v.toString());
            resultMap.put(k,v.toString());
        });

        return JSON.toJSONString(resultMap);
      //  result.all().get().forEach((k,v)->System.out.println("k: "+k+" ,v: "+v.toString()+"\n\r"));
    }

    @ApiOperation(value = "删除topic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicName", value = "topic名称",defaultValue = "first_top",
                    required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/delete")
    public String deleteTopic(String topicName){
        DeleteTopicsResult  result = adminClient.deleteTopics(Arrays.asList(topicName));
        return  JSON.toJSONString(result.values());
    }

}
