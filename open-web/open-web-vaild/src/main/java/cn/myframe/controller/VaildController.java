package cn.myframe.controller;

import cn.myframe.entity.VaildModel;
import cn.myframe.entity.group.GroupA;
import cn.myframe.entity.group.GroupB;
import cn.myframe.entity.group.Person;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

@RestController
public class VaildController {
    @RequestMapping("/vaild")
    public String vaild(@RequestBody(required = false) @Valid VaildModel model,
                        BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
        System.out.println(JSON.toJSONString(result.getAllErrors()));
        return "success";
    }

    @Autowired
    private Validator validator;

    @RequestMapping("/vaild3")
    public String vaild3(@RequestBody(required = false) VaildModel vaildModel){
        Set<ConstraintViolation<VaildModel>> violationSet = validator.validate(vaildModel);
        for (ConstraintViolation<VaildModel> model : violationSet) {
            System.out.println(model.getMessage());
        }
        return "success";
    }

    @RequestMapping("/vaild4")
    public String vaild4(@RequestBody @Validated({GroupA.class}) Person person,
                         BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
        System.out.println(JSON.toJSONString(result.getAllErrors()));
        return "success";
    }

    @RequestMapping("/vaild5")
    public String vaild5(@RequestBody(required = false) @Validated({GroupB.class,Default.class}) Person person,
                         BindingResult result){
        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
            }
        }
        System.out.println(JSON.toJSONString(result.getAllErrors()));
        return "success";
    }
}
