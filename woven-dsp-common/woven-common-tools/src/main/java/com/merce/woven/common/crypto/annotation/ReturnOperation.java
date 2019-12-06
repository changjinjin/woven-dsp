package com.merce.woven.common.crypto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.merce.woven.common.crypto.CryptoOperation;
import com.merce.woven.common.crypto.CryptoType;

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
     * 加解密方式
     *
     * @return 加解密方式
     */
    CryptoType cryptoType();
}
