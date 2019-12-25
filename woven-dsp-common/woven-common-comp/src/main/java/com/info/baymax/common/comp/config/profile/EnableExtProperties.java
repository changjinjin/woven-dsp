package com.info.baymax.common.comp.config.profile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PropertyResourceConfiguration.class)
public @interface EnableExtProperties {

    /**
     * 配置文件路径，可使用通配符，主要解决@PropertySource不能使用通配符的问题
     *
     * @return 文件路径
     */
    String[] value() default {};
}
