package com.info.baymax.dsp.auth.security.config.oauth2;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Oauth2Client {
    private String clientId;
    private String clientSecret;
    private String[] authorizedGrantTypes = {};
    private String[] authorities = {};
    private int accessTokenValiditySeconds = 60000;
    private int refreshTokenValiditySeconds = 60000;
    private String[] scopes = {};
    private String[] autoApproveScopes = {};
    private String[] redirectUris = {};
    private String[] resourceIds = {};
    private boolean autoApprove = true;
    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
}
