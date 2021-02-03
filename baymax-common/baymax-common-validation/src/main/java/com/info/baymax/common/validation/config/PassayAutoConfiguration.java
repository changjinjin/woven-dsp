package com.info.baymax.common.validation.config;

import com.info.baymax.common.validation.passay.check.PasswordChecker;
import com.info.baymax.common.validation.passay.check.SimplePasswordChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;

/**
 * 密码校验配置
 *
 * @author jingwei.yang
 * @date 2021年2月2日 下午2:54:58
 */
@Configuration
@EnableConfigurationProperties(value = PassayProperties.class)
public class PassayAutoConfiguration {

    @Autowired
    @Nullable
    private PassayProperties passayProperties;

    @Bean
    @ConditionalOnMissingBean(value = PasswordChecker.class)
    public PasswordChecker passwordChecker() {
        if (passayProperties == null) {
            passayProperties = new PassayProperties();
        }
        return new SimplePasswordChecker(passayProperties);
    }
}
