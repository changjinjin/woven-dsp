package com.info.baymax.common.comp.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Request;

@Configuration
public class FeignServiceClientConfiguration {

    @Value("${service.feign.connectTimeout:60000}")
    private int connectTimeout;

    @Value("${service.feign.readTimeOut:60000}")
    private int readTimeout;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeout, readTimeout);
    }

    @Bean
    public TransmitTokenFeighClientIntercepter TransmitTokenFeighClientIntercepter() {
        return new TransmitTokenFeighClientIntercepter();
    }
}
