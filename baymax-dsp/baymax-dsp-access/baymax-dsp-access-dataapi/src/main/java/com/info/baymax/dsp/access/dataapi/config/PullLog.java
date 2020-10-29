package com.info.baymax.dsp.access.dataapi.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PullLog {

    /**
     * 是否开启，默认开启
     *
     * @return 开启标志，true-开启，false-关闭
     */
    boolean value() default true;
}
