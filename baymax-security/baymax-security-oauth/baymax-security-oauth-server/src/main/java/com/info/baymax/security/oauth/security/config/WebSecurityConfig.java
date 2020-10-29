package com.info.baymax.security.oauth.security.config;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

import com.info.baymax.dsp.data.sys.crypto.check.PasswordChecker;
import com.info.baymax.dsp.data.sys.crypto.check.StrictModePasswordChecker;
import com.info.baymax.security.oauth.api.config.WhiteListProperties;
import com.info.baymax.security.oauth.security.authentication.customer.CustomerUserDetailsService;
import com.info.baymax.security.oauth.security.authentication.customer.TenantCustomerAuthenticationProvider;
import com.info.baymax.security.oauth.security.authentication.manager.ManagerUserDetailsService;
import com.info.baymax.security.oauth.security.authentication.manager.TenantManagerAuthenticationProvider;
import com.info.baymax.security.oauth.security.authentication.tenant.TenantDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
				.userDetailsService(managerUserDetailsService)//
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
		// .and().authorizeRequests()//
		// .antMatchers(AuthConstants.TOKEN_ENTRY_POINT).fullyAuthenticated()//
		// .antMatchers(AuthConstants.TOKEN_REFRESH_ENTRY_POINT).permitAll()//
		// .antMatchers(whiteListProperties.getAllWhiteList()).permitAll()//
		// .antMatchers(AuthConstants.TOKEN_AUTH_ENTRY_POINT).authenticated()//
		;
	}

	@Autowired
	private TenantDetailsService tenantDetailsService;
	@Autowired
	private ManagerUserDetailsService managerUserDetailsService;
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		AuthenticationManager authenticationManager2 = super.authenticationManager();
		if (authenticationManager2 instanceof ProviderManager) {
			ProviderManager providerManager = (ProviderManager) authenticationManager2;
			providerManager.getProviders().add(0, new TenantCustomerAuthenticationProvider(tenantDetailsService,
					customerUserDetailsService, passwordEncoder));
			providerManager.getProviders().add(0, new TenantManagerAuthenticationProvider(tenantDetailsService,
					managerUserDetailsService, passwordEncoder));
			return providerManager;
		}
		return super.authenticationManager();
	}

	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}
