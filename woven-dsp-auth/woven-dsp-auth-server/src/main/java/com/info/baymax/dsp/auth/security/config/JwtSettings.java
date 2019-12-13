package com.info.baymax.dsp.auth.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.token.jwt")
public class JwtSettings {

	/**
	 * access_token有效期
	 */
	private Integer accessTokenExpIn;

	/**
	 * refresh_token有效期
	 */
	private Integer refreshTokenExpIn;

	/**
	 * token issuer
	 */
	private String tokenIssuer;

	/**
	 * token signing key
	 */
	private String tokenSigningKey;

}
