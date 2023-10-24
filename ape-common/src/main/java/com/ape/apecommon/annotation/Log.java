package com.ape.apecommon.annotation;

import com.ape.apecommon.enums.BusinessType;
import com.ape.apecommon.enums.OperatorType;

import java.lang.annotation.*;

/**
* @description: 操作日志注解
* @author shaozhujie
* @date 2023/9/22 10:23
* @version 1.0
*/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 模块
     */
    String name() default "";

    /**
     * 功能
     */
    BusinessType type() default BusinessType.OTHER;

    /**
     * 操作人类别
     */
    OperatorType operType() default OperatorType.MANAGE;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;

}
