package com.info.baymax.common.core.crypto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 加密解密标记注解，用于标记该方法的参数是否有参数需要做解码处理和返回结果需要做加密处理，可通过开关打开或者关闭
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cryptoable {

    /**
     * 是否启用解码，默认启用
     *
     * @return 是否启用解密
     */
    boolean enable() default true;

    /**
     * 返回结果的加密解密操作可指定多种形式，默认不处理，需要显式指定
     *
     * @return 返回值加解密处理方式
     */
    ReturnOperation[] returnOperation() default {};

    /**
     * 是否需要处理参数，默认false
     *
     * @return 是否处理参数
     */
    boolean enableParam() default false;
}
