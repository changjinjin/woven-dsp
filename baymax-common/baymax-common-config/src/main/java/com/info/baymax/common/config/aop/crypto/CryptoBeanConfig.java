package com.info.baymax.common.config.aop.crypto;

import com.info.baymax.common.config.aop.crypto.CryptoBeanConfig.CryptoProperties;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.core.crypto.delegater.DefaultCryptorDelegater;
import com.info.baymax.common.core.crypto.method.CryptoConfig;
import com.info.baymax.common.core.crypto.method.CryptoMethodInvoker;
import com.info.baymax.common.core.crypto.method.DefaultCryptoMethodInvoker;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 接口参数和结果加密解密切面处理
 *
 * @author jingwei.yang
 * @date 2019年11月21日 上午11:08:14
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoBeanConfig {

    @Bean
    @ConditionalOnMissingBean(value = CryptoMethodInvoker.class)
    public CryptoMethodInvoker cryptoMethodInvoker(final CryptoConfig config) {
        return new DefaultCryptoMethodInvoker(config.getSecretKey());
    }

    @Bean
    @ConditionalOnMissingBean(value = CryptorDelegater.class)
    public CryptorDelegater cryptorDelegater() {
        return new DefaultCryptorDelegater();
    }

    @Setter
    @Getter
    @ConfigurationProperties(prefix = CryptoProperties.PREFIX)
    public static final class CryptoProperties extends CryptoConfig {
        public static final String PREFIX = "crypto";
    }
}
