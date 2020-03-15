package cn.myframe.controller;

import cn.myframe.dto.Item;
import cn.myframe.dto.UserDto;
import cn.myframe.repository.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: ynz
 * @Date: 2019/4/22/022 17:25
 * @Version 1.0
 */
@Repository("solrDao")
@RestController
public class SolrController {

    @Autowired
    private UserService userService;

    @RequestMapping("/add/{id}")
    public String add(@PathVariable  String id,@RequestBody String address){
        userService.save(new UserDto().setUserId(id).setAddress(address));
        return id;
    }

    @RequestMapping("/query/{id}")
    public String query(@RequestBody String address,@PathVariable String id){
        List<UserDto> list = userService.queryByPage(address,id);
        return JSON.toJSONString(list);
    }

    @RequestMapping("/queryHL")
    public String queryHL(@RequestBody String address){
        List<Item> list = userService.queryHeightLight(address);
        return JSON.toJSONString(list);
    }

    @RequestMapping("/upLoad")
    public String upLoad(){
         userService.upFile();
        return JSON.toJSONString("success");
    }



}
