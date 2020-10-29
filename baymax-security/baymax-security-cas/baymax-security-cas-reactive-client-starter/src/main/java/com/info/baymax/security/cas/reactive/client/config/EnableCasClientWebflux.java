package com.info.baymax.security.cas.reactive.client.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CasClientWebfluxAutoConfiguration.class)
public @interface EnableCasClientWebflux {
}
