package com.merce.woven.dsp.auth.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.merce.woven.dsp.auth.oauth2.exception.CustomAuthenticationExceptionEntryPoint;
import com.merce.woven.dsp.auth.oauth2.exception.CustomWebResponseExceptionTranslator;
import com.merce.woven.dsp.auth.security.config.WhiteListProperties;

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
				.anonymous().disable()//
				.httpBasic().and().exceptionHandling()//
				.authenticationEntryPoint(new CustomAuthenticationExceptionEntryPoint())//
				.and().authorizeRequests()//
//				.antMatchers(whiteListProperties.getAllWhiteList()).permitAll()//
				.anyRequest().authenticated();
	}
}
