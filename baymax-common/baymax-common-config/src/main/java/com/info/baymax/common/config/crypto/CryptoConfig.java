package com.info.baymax.common.config.crypto;

import com.info.baymax.common.config.crypto.CryptoConfig.CryptoProperties;
import com.info.baymax.common.core.crypto.delegater.CryptorDelegater;
import com.info.baymax.common.core.crypto.delegater.DefaultCryptorDelegater;
import com.info.baymax.common.core.crypto.method.CryptoMethodInvoker;
import lombok.Getter;
import lombok.Setter;
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
@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
public class CryptoConfig {

    @Bean
    public CryptoMethodInvoker cryptoMethodInvoker(final CryptoProperties properties) {
        return new CustomCryptoMethodInvoker(properties.getSecretKey(), cryptorDelegater());
    }

    @Bean
    public CryptorDelegater cryptorDelegater() {
        return new DefaultCryptorDelegater();
    }

    @Setter
    @Getter
    @ConfigurationProperties(prefix = CryptoProperties.PREFIX)
    public static final class CryptoProperties {
        public static final String PREFIX = "crypto";

        private String secretKey = "infoaeskey123456";
    }
}
