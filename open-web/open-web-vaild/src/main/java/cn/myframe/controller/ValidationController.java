package cn.myframe.controller;

import com.alibaba.fastjson.JSON;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Validated
public class ValidationController {

    /**如果只有少数对象，直接把参数写到Controller层，然后在Controller层进行验证就可以了。*/
    @RequestMapping(value = "/vaild2", method = RequestMethod.GET)
    public String vaild2(@Range(min = 1, max = 9, message = "年级只能从1-9")
                      @RequestParam(name = "grade", required = true)
                              int grade,
                      @Min(value = 1, message = "班级最小只能1")
                      @Max(value = 99, message = "班级最大只能99")
                      @RequestParam(name = "classroom", required = true)
                              int classroom) {
        System.out.println(grade + "," + classroom);
        return grade + "," + classroom;
    }

    /**
     * 验证不通过时，抛出了ConstraintViolationException异常，使用同一捕获异常处理
     */
    @ControllerAdvice
    @Component
     class GlobalExceptionHandler {
        @ExceptionHandler(ConstraintViolationException.class)
        @ResponseBody
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public String handle(ValidationException exception) {
            List<String> errorMsg = new ArrayList<>();
            if(exception instanceof ConstraintViolationException){
                ConstraintViolationException exs = (ConstraintViolationException) exception;

                Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
                for (ConstraintViolation<?> item : violations) {
                    /**打印验证不通过的信息*/
                    System.out.println(item.getMessage());
                    errorMsg.add(item.getMessage());
                }
            }
            return "bad request, " +JSON.toJSONString(errorMsg);
        }
    }
}
