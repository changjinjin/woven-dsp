package com.info.baymax.dsp.data.dataset.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface ValueBind {
    enum fieldType{
        STRING,INT;
    }

    fieldType FieldType() default fieldType.STRING;

    int[] EnumIntValues() default 0;

    String[] EnumStringValues() default "";
}
