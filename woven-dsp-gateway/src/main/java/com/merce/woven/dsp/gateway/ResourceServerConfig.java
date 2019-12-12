package com.merce.woven.dsp.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private WhiteListProperties whiteListProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http//
				.csrf().disable()//
				.anonymous().disable()//
				.httpBasic()//
				.and().authorizeRequests()//
				.antMatchers(whiteListProperties.getAllWhiteList()).permitAll()//
				.anyRequest().authenticated();
	}
}
