package com.info.baymax.security.oauth.security.config.oauth2;

import com.info.baymax.security.oauth.security.authentication.AbstractTenantUserAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

public class TenantAuthenticationKeyGenerator extends DefaultAuthenticationKeyGenerator {
	private static final String CLIENT_ID = "client_id";
	private static final String SCOPE = "scope";
	private static final String TENANT = "tenant";
	private static final String USERNAME = "username";

	@Override
	public String extractKey(OAuth2Authentication authentication) {

		Map<String, String> values = new LinkedHashMap<String, String>();
		OAuth2Request authorizationRequest = authentication.getOAuth2Request();
		Authentication userAuthentication = authentication.getUserAuthentication();
		if (userAuthentication instanceof AbstractTenantUserAuthenticationToken) {
			AbstractTenantUserAuthenticationToken userAuth = (AbstractTenantUserAuthenticationToken) userAuthentication;
			values.put(TENANT, userAuth.getTenant());
		}

		if (!authentication.isClientOnly()) {
			values.put(USERNAME, authentication.getName());
		}
		values.put(CLIENT_ID, authorizationRequest.getClientId());
		if (authorizationRequest.getScope() != null) {
			values.put(SCOPE, OAuth2Utils.formatParameterList(new TreeSet<String>(authorizationRequest.getScope())));
		}
		return generateKey(values);
	}

}
