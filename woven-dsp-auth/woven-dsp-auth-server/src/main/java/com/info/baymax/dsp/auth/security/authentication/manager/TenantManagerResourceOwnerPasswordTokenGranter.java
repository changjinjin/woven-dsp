package com.info.baymax.dsp.auth.security.authentication.manager;

import com.info.baymax.dsp.auth.security.authentication.AbstractTenantUserResourceOwnerPasswordTokenGranter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class TenantManagerResourceOwnerPasswordTokenGranter
    extends AbstractTenantUserResourceOwnerPasswordTokenGranter {
    private static final String GRANT_TYPE = "manager_password";

    public TenantManagerResourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager,
                                                          AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
                                                          OAuth2RequestFactory requestFactory) {
        super(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
    }

    @Override
    public Authentication extractedAuthentication(ClientDetails client, String username, String password, String tenant,
                                                  String version) {
        return new TenantManagerAuthenticationToken(username, password, client.getClientId(), tenant, version);
    }
}
