package com.info.baymax.dsp.auth.config.locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setDefaultLocale(Locale.CHINA);
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource parentMessageSource = new ReloadableResourceBundleMessageSource();
        parentMessageSource.setDefaultEncoding("UTF-8");
        // parentMessageSource.setBasename("classpath:org/springframework/security/messages");
        parentMessageSource.setBasename("classpath:i18n/messages");
        return parentMessageSource;
    }
}
