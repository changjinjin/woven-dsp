package com.info.baymax.dsp.auth.oauth2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.info.baymax.dsp.auth.api.exception.CustomWebResponseExceptionTranslator;
import com.info.baymax.dsp.auth.oauth2.authentication.TenantUserResourceOwnerPasswordTokenGranter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	public ClientDetailsService inMemoryClientDetailsService() {
		return new InMemoryClientDetailsService();
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)//
				// .tokenEnhancer(tokenEnhancer())//
				// 配置JwtAccessToken转换器
				// .accessTokenConverter(accessTokenConverter())
				// refresh_token需要userDetailsService
				.reuseRefreshTokens(false)//
				.userDetailsService(userDetailsService)//
				.tokenStore(tokenStore())//
				.allowedTokenEndpointRequestMethods(HttpMethod.POST, HttpMethod.GET, HttpMethod.OPTIONS)//
				.exceptionTranslator(new CustomWebResponseExceptionTranslator());

		// 添加自定义的TokenGranters
		List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getTokenServices(),
				endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
		tokenGranters.add(endpoints.getTokenGranter());
		endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
	}

	private List<TokenGranter> getTokenGranters(AuthorizationServerTokenServices tokenServices,
			ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		return new ArrayList<>(Arrays.asList(new TenantUserResourceOwnerPasswordTokenGranter(authenticationManager,
				tokenServices, clientDetailsService, requestFactory)));
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security //
				.tokenKeyAccess("permitAll()")//
				.checkTokenAccess("isAuthenticated()") //
				.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
		builder//
				.withClient("baymax")//
				.secret(passwordEncoder.encode("123456"))//
				.scopes("read", "write", "trust")//
				.autoApprove(true)//
				.authorities("WRIGTH_READ")//
				.accessTokenValiditySeconds(60000)//
				.refreshTokenValiditySeconds(72000)//
				.authorizedGrantTypes("refresh_token", "authorization_code", "password", "tenant_password",
						"client_credentials")//
				.and()//
				.withClient("dsp")//
				.secret(passwordEncoder.encode("123456"))//
				.scopes("read", "write", "trust")//
				.autoApprove(true)//
				.accessTokenValiditySeconds(60000)//
				.refreshTokenValiditySeconds(72000)//
				.authorities("WRIGTH_READ")//
				.authorizedGrantTypes("refresh_token", "authorization_code", "password", "tenant_password",
						"client_credentials");
	}
}