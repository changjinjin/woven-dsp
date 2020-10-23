package com.merce.woven.cas.client.reactive.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(CasClientWebfluxAutoConfiguration.class)
public @interface EnableCasClientWebflux {
}
