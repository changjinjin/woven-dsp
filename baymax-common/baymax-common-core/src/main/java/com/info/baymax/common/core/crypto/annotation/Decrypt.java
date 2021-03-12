package com.info.baymax.common.core.crypto.annotation;

import com.info.baymax.common.core.crypto.CryptoDecoder;
import com.info.baymax.common.core.crypto.CryptoType;
import com.info.baymax.common.core.crypto.decoder.NoneCryptoDecoder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要做解密处理的参数注解
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午9:49:56
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Decrypt {

    /**
     * 是否有加密算法标志包裹，如：使用AES加密后 密文通过 “AES()”包裹了，则该属性应为true，否则为false，默认true
     *
     * @return 密文是否被包裹
     */
    boolean wrapped() default true;

    /**
     * 加密方式，当<code>wrapped()</code>为false是生效
     *
     * @return 加密类型
     */
    CryptoType cryptoType() default CryptoType.NONE;

    /**
     * 解密器类
     *
     * @return 解密器类
     */
    Class<? extends CryptoDecoder<?>> decoder() default NoneCryptoDecoder.class;
}
