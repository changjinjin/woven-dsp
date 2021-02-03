/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.info.baymax.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 实体序列化json字段定制注解，一般用于Rest接口方法上，过滤返回数据的序列化属性达到报文定制的目的，JsonBody的复数形式
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:49:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonBodys {

    /**
     * 支持同时过滤多个数据类型的字段
     *
     * @return 过滤配置数组
     */
    JsonBody[] value() default {};

}