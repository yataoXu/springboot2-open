package cn.myframe.controller;

import cn.myframe.mvc.annotation.ActionMapping;
import cn.myframe.mvc.annotation.ResponseType;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author ynz
 * @version 创建时间：2018/6/25
 * @email ynz@myframe.cn
 */
@Component
@ActionMapping(actionKey="controller")
@ResponseType
public class HttpNettyController implements BaseActionController{

    @ActionMapping(actionKey = "method")
    public String method(String a, String b){

        return String.format("a:%s,b:%s",a,b);
    }
}
