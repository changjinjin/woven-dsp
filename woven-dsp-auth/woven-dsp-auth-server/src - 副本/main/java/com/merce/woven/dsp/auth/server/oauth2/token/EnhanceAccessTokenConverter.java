package com.jusfoun.services.auth.server.oauth2.token;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.jusfoun.services.auth.api.Constant;
import com.jusfoun.services.ops.api.entity.sys.WfSysUser;

public class EnhanceAccessTokenConverter extends DefaultAccessTokenConverter implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

		// 设置额外用户信息
		WfSysUser sysUser = ((TokenUserDetails) authentication.getPrincipal()).getSysUser();
		sysUser.setPassword(null);
		// 将用户信息添加到token额外信息中
		defaultOAuth2AccessToken.getAdditionalInformation().put(Constant.USER_INFO, sysUser);

		return defaultOAuth2AccessToken;
	}
}
