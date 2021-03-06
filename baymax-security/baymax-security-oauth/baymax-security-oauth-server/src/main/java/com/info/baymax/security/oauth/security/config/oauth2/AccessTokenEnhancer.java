package com.info.baymax.security.oauth.security.config.oauth2;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.info.baymax.security.oauth.api.utils.SecurityUtils;
import com.info.baymax.security.oauth.security.authentication.AbstractTenantUserAuthenticationToken;

public class AccessTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        return additionalToken(accessToken, authentication);
    }

    public static DefaultOAuth2AccessToken additionalToken(OAuth2AccessToken accessToken,
                                                           OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken enhancedAccessToken = new DefaultOAuth2AccessToken(accessToken);
        // 附加信息
        Map<String, Object> additionalInfo = new HashMap<String, Object>();
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication instanceof AbstractTenantUserAuthenticationToken) {
            AbstractTenantUserAuthenticationToken token = (AbstractTenantUserAuthenticationToken) userAuthentication;
            additionalInfo.put("userinfo", SecurityUtils.getSaasContextFromToken(token));
        }
        enhancedAccessToken.setAdditionalInformation(additionalInfo);
        return enhancedAccessToken;
    }
}
