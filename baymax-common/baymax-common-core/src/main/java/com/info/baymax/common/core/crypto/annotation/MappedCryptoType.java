package com.info.baymax.common.core.crypto.annotation;

import com.info.baymax.common.core.crypto.CryptoType;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedCryptoType {
    CryptoType value();
}
