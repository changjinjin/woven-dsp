/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.info.baymax.common.comp.serialize.annotation;

import java.lang.annotation.*;

/**
 * Number数据序列化注解
 *
 * @author jingwei.yang
 * @date 2019-05-29 16:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface NumberFormat {

    /**
     * 数据格式，如：####.####
     *
     * @return 数据格式
     */
    String pattern() default "";

    /**
     * 对序列化是否生效
     *
     * @return 对序列化是否生效，默认生效
     */
    boolean serialize() default true;

    /**
     * 对反序列化是否生效
     *
     * @return 对反序列化是否生效，默认不生效
     */
    boolean deserialize() default false;

}
