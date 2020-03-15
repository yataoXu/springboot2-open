package cn.myframe.entity.group;

import cn.myframe.validator.CaseMode;
import cn.myframe.validator.CheckCase;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.groups.Default;

@Data
public class Person {
    @NotBlank(message = "不能为空",groups = {GroupA.class})
    /**用户id*/
    private Integer userId;

    @Length(min = 4,max = 20,message = "必须在[4,20]",groups = {GroupB.class})
    @Length(min = 1,max = 3,message = "必须在[1,3]",groups = {GroupA.class})
    @CheckCase(value = CaseMode.LOWER ,message = "必须小写",groups = {Default.class})
    /**用户名*/
    private String userName;

}
