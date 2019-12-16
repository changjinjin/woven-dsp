package com.info.baymax.dsp.auth.oauth2.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = Oauth2ClientProperties.PREFIX)
public class Oauth2ClientProperties {
    public static final String PREFIX = "security.oauth2";

    private List<Oauth2Client> clients;
}
