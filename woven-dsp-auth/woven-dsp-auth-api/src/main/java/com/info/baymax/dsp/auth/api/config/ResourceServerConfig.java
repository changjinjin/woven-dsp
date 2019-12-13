package com.info.baymax.dsp.auth.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.info.baymax.dsp.auth.api.exception.CustomAuthenticationExceptionEntryPoint;
import com.info.baymax.dsp.auth.api.exception.CustomWebResponseExceptionTranslator;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private WhiteListProperties whiteListProperties;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		// 定义异常转换类生效
		AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
		((OAuth2AuthenticationEntryPoint) authenticationEntryPoint)
				.setExceptionTranslator(new CustomWebResponseExceptionTranslator());
		resources.authenticationEntryPoint(authenticationEntryPoint);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http//
				.csrf().disable()//
				// .anonymous().disable()//
				// .httpBasic()//
				.exceptionHandling()//
				.authenticationEntryPoint(new CustomAuthenticationExceptionEntryPoint())//
				.and().authorizeRequests()//
				.antMatchers(whiteListProperties.getAllWhiteList()).permitAll()//
				.anyRequest().authenticated();
	}
}
