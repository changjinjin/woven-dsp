package com.info.baymax.common.crypto.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.info.baymax.common.crypto.CryptoType;

/**
 * 需要做加密处理的参数注解
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:54:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Encrypt {

    /**
     * 加密方式
     *
     * @return 加密方式
     */
    CryptoType cryptoType();
}
