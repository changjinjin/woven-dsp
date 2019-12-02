package com.jusfoun.services.auth.server.oauth2.token.jwt;

import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import com.jusfoun.common.utils.JsonUtils;
import com.jusfoun.services.auth.api.Constant;
import com.jusfoun.services.auth.server.oauth2.token.TokenUserDetails;
import com.jusfoun.services.ops.api.entity.sys.WfSysUser;

/**
 * 说明：自定义JwtAccessToken转换器.<br>
 * 
 * @author yjw
 * @date 2018年12月1日 下午3:42:41
 */
public class EnhanceJwtAccessTokenConverter extends JwtAccessTokenConverter {

	/**
	 * 生成token
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);

		// 设置额外用户信息
		WfSysUser sysUser = ((TokenUserDetails) authentication.getPrincipal()).getSysUser();
		sysUser.setPassword(null);
		// 将用户信息添加到token额外信息中
		defaultOAuth2AccessToken.getAdditionalInformation().put(Constant.USER_INFO, sysUser);

		return super.enhance(defaultOAuth2AccessToken, authentication);
	}

	@Override
	public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
		OAuth2AccessToken oauth2AccessToken = super.extractAccessToken(value, map);
		convertData(oauth2AccessToken, oauth2AccessToken.getAdditionalInformation());
		return oauth2AccessToken;
	}

	private void convertData(OAuth2AccessToken accessToken, Map<String, ?> map) {
		accessToken.getAdditionalInformation().put(Constant.USER_INFO, convertUserData(map.get(Constant.USER_INFO)));
	}

	private WfSysUser convertUserData(Object map) {
		String json = JsonUtils.deserializer(map);
		WfSysUser user = JsonUtils.serializable(json, WfSysUser.class);
		return user;
	}
}
