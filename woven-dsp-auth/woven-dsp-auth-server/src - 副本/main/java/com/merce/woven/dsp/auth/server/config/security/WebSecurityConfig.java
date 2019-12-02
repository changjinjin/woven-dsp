package com.merce.woven.dsp.auth.server.config.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.jusfoun.common.base.constants.AuthConstants;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.KaptchaProperties;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.filter.KaptchAuthenticationFailureHandler;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.filter.KaptchaAuthenticationProvider;
import com.merce.woven.dsp.auth.server.oauth2.kaptcha.filter.KaptchaValidateFilter;

/**
 * 描述 : 安全配置. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月6日 下午3:13:00
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true) // 开启方法权限验证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// @Autowired
	// private AuthenticationManager authenticationManager;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private KaptchaProperties kaptchaProperties;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider kaptchaAuthenticationProvider() {
		return new KaptchaAuthenticationProvider(cacheManager);
	}

	@Bean
	public AuthenticationFailureHandler kaptchAuthenticationFailureHandler() {
		return new KaptchAuthenticationFailureHandler();
	}

	// @Bean
	public Filter kaptchaValidateFilter() {
		return new KaptchaValidateFilter(cacheManager, kaptchAuthenticationFailureHandler(),
				kaptchaProperties.getInterceptUrls());
	}

	// @Bean
	// public Filter kaptchaAuthenticationProcessingFilter() throws Exception {
	// KaptchaAuthenticationProcessingFilter
	// kaptchaAuthenticationProcessingFilter = new
	// KaptchaAuthenticationProcessingFilter(
	// cacheManager);
	// kaptchaAuthenticationProcessingFilter.setAuthenticationManager(authenticationManager);
	// return kaptchaAuthenticationProcessingFilter;
	// }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth//
				.userDetailsService(userDetailsService)//
				.passwordEncoder(passwordEncoder())//
				.and()//
				.authenticationProvider(kaptchaAuthenticationProvider())//
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/image/**", "/**/*.js", "/**/*.css", "/resource/img/**")
				.antMatchers(AuthConstants.AUTH_SWAGGER2_WHITELIST);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http//
			// .addFilterBefore(kaptchaValidateFilter(),
			// UsernamePasswordAuthenticationFilter.class) //
				.csrf().disable() //
				.anonymous().disable()//
				// .exceptionHandling().authenticationEntryPoint(basicAuthenticationEntryPoint)//
				// .and()//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and()//
				.authorizeRequests()//
				.antMatchers(AuthConstants.TOKEN_ENTRY_POINT).fullyAuthenticated() //
				.antMatchers(AuthConstants.TOKEN_REFRESH_ENTRY_POINT).permitAll()//
				.antMatchers(AuthConstants.TOKEN_REVOKE_ENTRY_POINT).permitAll()//
				.antMatchers(AuthConstants.AUTH_SWAGGER2_WHITELIST).permitAll()//
				.antMatchers(AuthConstants.TOKEN_AUTH_ENTRY_POINT).authenticated(); //
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}

}
