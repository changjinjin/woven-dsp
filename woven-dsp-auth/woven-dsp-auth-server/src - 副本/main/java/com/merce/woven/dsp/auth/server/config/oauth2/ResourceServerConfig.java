package com.merce.woven.dsp.auth.server.config.oauth2;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 描述 : Resource服务配置. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月6日 下午3:12:28
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http//
				.csrf().disable()//
				.anonymous().disable()//
				.httpBasic()//
				.and().exceptionHandling()//
				.authenticationEntryPoint(
						(request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))//
				.and().authorizeRequests()//
				.anyRequest().authenticated();
	}

	// @Override
	// public void configure(HttpSecurity http) throws Exception {
	// OAuth2AuthenticationProcessingFilter f = new
	// OAuth2AuthenticationProcessingFilter();
	// OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint = new
	// OAuth2AuthenticationEntryPoint();
	// oAuth2AuthenticationEntryPoint.setExceptionTranslator(webResponseExceptionTranslator());
	// f.setAuthenticationEntryPoint(oAuth2AuthenticationEntryPoint);
	// OAuth2AuthenticationManager o = new OAuth2AuthenticationManager();
	// DefaultTokenServices dt = new DefaultTokenServices();
	// dt.setTokenStore(tokenStore());
	// o.setTokenServices(dt);
	// f.setAuthenticationManager(o);
	// http.antMatcher("/api/**/**").authorizeRequests().anyRequest().authenticated().and().addFilterBefore(f,
	// AbstractPreAuthenticatedProcessingFilter.class);
	// }

}
