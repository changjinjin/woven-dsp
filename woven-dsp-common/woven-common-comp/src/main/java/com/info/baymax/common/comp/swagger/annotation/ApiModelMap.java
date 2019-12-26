package com.info.baymax.common.comp.swagger.annotation;

import io.swagger.annotations.ApiModelProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelMap {

    /**
     * 对象属性文档列表
     *
     * @return 属性文档列表
     */
    ApiModelProperty[] value() default {};
}