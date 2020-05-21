package com.info.baymax.common.crypto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.info.baymax.common.crypto.CryptoOperation;
import com.info.baymax.common.crypto.CryptoType;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReturnOperation {

    /**
     * 返回结果的加密解密操作类型，默认不处理，需要显式指定
     *
     * @return 加密解密类型
     */
    CryptoOperation cryptoOperation() default CryptoOperation.None;

    /**
     * 是否有加密算法标志包裹，如：使用AES加密后 密文通过 “AES()”包裹了，则该属性应为true，否则为false，默认true
     *
     * @return 密文是否被包裹
     */
    boolean wrapped() default true;

    /**
     * 加解密方式
     *
     * @return 加解密方式
     */
    CryptoType cryptoType();
}
