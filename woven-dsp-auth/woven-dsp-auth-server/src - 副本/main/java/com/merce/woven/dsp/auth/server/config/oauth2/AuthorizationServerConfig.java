package com.merce.woven.dsp.auth.server.config.oauth2;

import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.google.common.collect.Maps;
import com.jusfoun.services.ops.api.entity.sys.WfSysRole;
import com.jusfoun.services.ops.api.entity.sys.WfSysUser;
import com.merce.woven.common.utils.ICollections;
import com.merce.woven.dsp.auth.api.Constant;
import com.merce.woven.dsp.auth.server.oauth2.exception.CustomAuthenticationExceptionEntryPoint;
import com.merce.woven.dsp.auth.server.oauth2.exception.CustomWebResponseExceptionTranslator;
import com.merce.woven.dsp.auth.server.oauth2.handler.CustomAccessDeniedHandler;
import com.merce.woven.dsp.auth.server.oauth2.token.TokenUserDetails;

/**
 * 描述 :OAuth2服务配置.<br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月6日 下午3:09:44
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private CustomWebResponseExceptionTranslator customWebResponseExceptionTranslator;

	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Bean // 声明 ClientDetails实现
	public ClientDetailsService clientDetails() {
		return new JdbcClientDetailsService(dataSource);
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new TokenEnhancer() {
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
				if (accessToken instanceof DefaultOAuth2AccessToken) {
					DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
					// 设置额外用户信息
					WfSysUser sysUser = ((TokenUserDetails) authentication.getPrincipal()).getSysUser();
					// 将用户信息添加到token额外信息中
					Map<String, Object> additionalInformation = Maps.newLinkedHashMap();
					Map<String, Object> info = Maps.newHashMap();
					info.put("userId", sysUser.getId());
					info.put("username", sysUser.getUsername());
					info.put("realName", sysUser.getRealName());
					info.put("isAdmin", sysUser.getIsAdmin());
					Set<String> authorities = sysUser.getAuthorities();
					if (ICollections.hasElements(authorities)) {
						info.put("authorities", authorities);
					} else {
						info.put("authorities", null);
					}
					WfSysRole role = sysUser.getRole();
					if (role != null) {
						info.put("roles", new String[] { role.getRoleName() });
					} else {
						info.put("roles", null);
					}
					additionalInformation.put(Constant.USER_INFO, info);
					token.setAdditionalInformation(additionalInformation);
				}
				return accessToken;
			}
		};
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)//
				.tokenEnhancer(tokenEnhancer())//
				// 配置JwtAccessToken转换器
				// .accessTokenConverter(accessTokenConverter())
				// refresh_token需要userDetailsService
				.reuseRefreshTokens(false)//
				.userDetailsService(userDetailsService)//
				.tokenStore(tokenStore())//
				.exceptionTranslator(customWebResponseExceptionTranslator);

	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security//
				// 开启/oauth/token_key验证端口无权限访问
				.tokenKeyAccess("permitAll()")//
				// 开启/oauth/check_token验证端口认证权限访问
				.checkTokenAccess("isAuthenticated()")
				.authenticationEntryPoint(new CustomAuthenticationExceptionEntryPoint())//
				.accessDeniedHandler(customAccessDeniedHandler);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.jdbc(dataSource);
	}

	/*
	 * @Bean public AccessTokenConverter accessTokenConverter() { final
	 * JwtAccessTokenConverter accessTokenConverter = new
	 * EnhanceJwtAccessTokenConverter(); // 导入证书 KeyStoreKeyFactory
	 * keyStoreKeyFactory = new KeyStoreKeyFactory(new
	 * ClassPathResource("keystore.jks"), "jusfoun".toCharArray());
	 * accessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair("jusfoun"))
	 * ; return accessTokenConverter; }
	 */

}
