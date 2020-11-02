package com.info.baymax.common.comp.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public MessageSourceAccessor messageSourceAccessor(final MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.SIMPLIFIED_CHINESE);
    }
}
