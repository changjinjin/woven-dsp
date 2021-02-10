package com.info.baymax.common.config.profile;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
