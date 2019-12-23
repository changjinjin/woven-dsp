package com.info.baymax.dsp.job.exec.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Value("${woven.rest.internal.token:merce}")
    private String restInternalToken;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    FormEncoder feignFormEncoder() {
        return new FormEncoder(new SpringEncoder(this.messageConverters));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignBasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new FeignBasicAuthRequestInterceptor(restInternalToken);
    }
}

class FeignBasicAuthRequestInterceptor  implements RequestInterceptor {

    private String internalToken;

    public FeignBasicAuthRequestInterceptor(String restInternalToken) {
        this.internalToken = restInternalToken;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("INTERNAL-TOKEN", internalToken);
    }
}