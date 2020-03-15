package cn.myframe.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author: ynz
 * @Date: 2019/4/16/016 17:58
 * @Version 1.0
 */
public class MyImportSelector2 implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        // 返回一个自定义类
        // 使用MyImportSelector导入MyConfiguration，而不是直接导入MyConfiguration
        return new String[]{MyConfiguration2.class.getName()};
    }
}
