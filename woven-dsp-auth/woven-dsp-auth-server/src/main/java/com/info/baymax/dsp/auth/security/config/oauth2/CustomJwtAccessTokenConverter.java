package com.info.baymax.dsp.auth.security.config.oauth2;

import com.info.baymax.dsp.auth.api.utils.SecurityUtils;
import com.info.baymax.dsp.auth.security.authentication.AbstractTenantUserAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        OAuth2AccessToken enhancedAccessToken = super.enhance(accessToken, authentication);

        // 附加信息
        Map<String, Object> additionalInfo = new HashMap<String, Object>();
        Authentication userAuthentication = authentication.getUserAuthentication();
        if (userAuthentication instanceof AbstractTenantUserAuthenticationToken) {
            AbstractTenantUserAuthenticationToken token = (AbstractTenantUserAuthenticationToken) userAuthentication;
            additionalInfo.put("userinfo", SecurityUtils.getSaasContextFromToken(token));
        }
        ((DefaultOAuth2AccessToken) enhancedAccessToken).setAdditionalInformation(additionalInfo);
        return enhancedAccessToken;
    }
}
