package com.info.baymax.dsp.auth.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import com.info.baymax.dsp.auth.api.config.WhiteListProperties;
import com.info.baymax.dsp.auth.oauth2.authentication.TenantUserAuthenticationProvider;
import com.info.baymax.dsp.auth.security.support.tenant.TenantDetailsService;
import com.info.baymax.dsp.data.sys.constant.AuthConstants;
import com.info.baymax.dsp.data.sys.crypto.check.PasswordChecker;
import com.info.baymax.dsp.data.sys.crypto.check.StrictModePasswordChecker;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private WhiteListProperties whiteListProperties;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public PasswordChecker passwordChecker() {
		return new StrictModePasswordChecker();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth//
				.userDetailsService(userDetailsService)//
				.passwordEncoder(passwordEncoder)//
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(whiteListProperties.getAllWhiteList());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.//
				csrf().disable() //
				.anonymous().disable()//
				.httpBasic().disable()//
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//
				.and().headers().frameOptions().disable()//
				.and().authorizeRequests()//
				.antMatchers(AuthConstants.TOKEN_ENTRY_POINT).fullyAuthenticated()//
				.antMatchers(AuthConstants.TOKEN_REFRESH_ENTRY_POINT).permitAll()//
				.antMatchers(whiteListProperties.getAllWhiteList()).permitAll()//
				.antMatchers(AuthConstants.TOKEN_AUTH_ENTRY_POINT).authenticated()//
		// .accessDecisionManager(defaultAccessDecisionManager)//
		// .and().apply(loginAuthenticationSecurityConfig)//
		// .and().apply(tokenAuthenticationSecurityConfig)//
		// .and().apply(kaptchaAuthenticationConfig)//
		;
	}

	@Autowired
	private TenantDetailsService tenantDetailsService;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		AuthenticationManager authenticationManager2 = super.authenticationManager();
		if (authenticationManager2 instanceof ProviderManager) {
			ProviderManager providerManager = (ProviderManager) authenticationManager2;
			providerManager.getProviders().add(0,
					new TenantUserAuthenticationProvider(tenantDetailsService, userDetailsService, passwordEncoder));
			return providerManager;
		}
		return super.authenticationManager();
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
