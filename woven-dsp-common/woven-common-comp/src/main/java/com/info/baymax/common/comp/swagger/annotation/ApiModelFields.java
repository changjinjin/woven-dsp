package com.info.baymax.common.comp.swagger.annotation;

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
     * 选填字段数组
     *
     * @return 选填字段数组
     */
    String[] filterFields() default {};

    /**
     * 是否是包含模式，如果是则filterFields里的字段需要包含进来，否则filterFields的字段需要排除
     *
     * @return 包含模式-true，排除模式-false
     */
    boolean includeMode() default true;
}