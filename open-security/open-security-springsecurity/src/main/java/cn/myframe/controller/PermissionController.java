package cn.myframe.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ynz
 * @Date: 2019/6/28/028 11:32
 * @Version 1.0
 */
@RestController
public class PermissionController {

    @RequestMapping("/perm")
    @PreAuthorize("hasPermission('/perm','perm')")
    public String perm(){
        return "success";
    }
}
