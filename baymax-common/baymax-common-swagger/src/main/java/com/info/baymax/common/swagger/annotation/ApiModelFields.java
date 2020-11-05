package com.info.baymax.common.swagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ApiModel字段处理注解，可配置Model的必填字段、选填字段、排除字段信息
 */
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelFields {

    /**
     * 必填字段数组
     *
     * @return 必填字段数组
     */
    String[] requiredFields() default {};

    /**
     * 隐藏字段数组
     *
     * @return 隐藏字段数组
     */
    String[] hiddenFields() default {};
}