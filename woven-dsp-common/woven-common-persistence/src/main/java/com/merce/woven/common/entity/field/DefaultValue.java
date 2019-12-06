package com.merce.woven.common.entity.field;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.merce.woven.common.entity.field.convertor.UnknownValueConvertor;
import com.merce.woven.common.entity.field.convertor.ValueConvertor;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DefaultValue {

    /**
     * 默认值
     *
     * @return 设置的默认值
     */
    String value() default "";

    /**
     * 值转换器类型
     *
     * @return 值转换器类型
     */
    Class<? extends ValueConvertor<?>> convertor() default UnknownValueConvertor.class;
}
