package com.info.baymax.dsp.auth.security.config.oauth2;

import com.info.baymax.dsp.auth.security.authentication.AbstractTenantUserAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<String, Object>();
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication instanceof AbstractTenantUserAuthenticationToken) {
            AbstractTenantUserAuthenticationToken token = (AbstractTenantUserAuthenticationToken) userAuthentication;
            additionalInfo.put("username", token.getName());
            additionalInfo.put("clientId", token.getClientId());
            additionalInfo.put("tenant", token.getTenant());
            additionalInfo.put("version", token.getTenantId());
            additionalInfo.put("userId", token.getUserId());
            additionalInfo.put("tenantId", token.getTenantId());
            additionalInfo.put("admin", token.isAdmin());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
