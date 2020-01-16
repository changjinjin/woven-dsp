package com.info.baymax.common.comp.serialize.annotation;

import java.lang.annotation.*;

/**
 * 实体序列化json字段定制注解，一般用于Rest接口方法上，过滤返回数据的序列化属性达到报文定制的目的
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:48:37
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JsonBodys.class)
public @interface JsonBody {

    /**
     * 需要过滤字段的类型
     *
     * @return 过滤类
     */
    Class<?> type();

    /**
     * 需要序列换的字段名数组
     *
     * @return 序列化的字段名数组
     */
    String[] includes() default {};

    /**
     * 需要忽略掉的字段名数组
     *
     * @return 忽略掉的字段名数组
     */
    String[] excludes() default {};

}
