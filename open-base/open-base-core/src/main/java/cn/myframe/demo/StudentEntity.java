package cn.myframe.demo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: ynz
 * @Date: 2019/2/26/026 17:02
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@Builder
public class StudentEntity {

    private String name;
    private int age;

    public static void main(String[] args) {
        StudentEntity studentEntity = StudentEntity.builder()
                .age(10).name("tom").build();
    }


}
