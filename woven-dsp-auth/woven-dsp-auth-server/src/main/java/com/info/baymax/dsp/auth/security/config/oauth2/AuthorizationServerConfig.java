package com.info.baymax.dsp.auth.security.config.oauth2;

import com.info.baymax.dsp.auth.api.exception.CustomWebResponseExceptionTranslator;
import com.info.baymax.dsp.auth.security.authentication.CustomTokenServices;
import com.info.baymax.dsp.auth.security.authentication.GrantedAuthoritiesService;
import com.info.baymax.dsp.auth.security.authentication.customer.TenantCustomerResourceOwnerPasswordTokenGranter;
import com.info.baymax.dsp.auth.security.authentication.manager.TenantManagerResourceOwnerPasswordTokenGranter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private GrantedAuthoritiesService grantedAuthoritiesService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Oauth2ClientProperties oauth2ClientProperties;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Bean
    public ClientDetailsService inMemoryClientDetailsService() {
        return new InMemoryClientDetailsService();
    }

    private CustomTokenServices customTokenServices(AuthorizationServerEndpointsConfigurer endpoints) {
        CustomTokenServices tokenServices = new CustomTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);// 支持刷新token
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//
            .tokenEnhancer(new AccessTokenEnhancer())//
            // .accessTokenConverter(new CustomJwtAccessTokenConverter())//
            // refresh_token需要userDetailsService
            .reuseRefreshTokens(false)//
            .tokenStore(tokenStore())//
            .tokenServices(customTokenServices(endpoints))//
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
        return new ArrayList<>(Arrays.asList(
            new TenantManagerResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                clientDetailsService, requestFactory),
            new TenantCustomerResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices,
                clientDetailsService, requestFactory)));
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

        List<Oauth2Client> clientDetails = oauth2ClientProperties.getClients();
        if (clientDetails != null && clientDetails.size() > 0) {
            for (Oauth2Client c : clientDetails) {
                builder//
                    .withClient(c.getClientId())//
                    .secret(passwordEncoder.encode(c.getClientSecret()))//
                    .resourceIds(c.getResourceIds())//
                    .scopes(c.getScopes())//
                    .autoApprove(c.isAutoApprove())//
                    .autoApprove(c.getAutoApproveScopes())//
                    .accessTokenValiditySeconds(c.getRefreshTokenValiditySeconds())//
                    .refreshTokenValiditySeconds(c.getRefreshTokenValiditySeconds())//
                    .authorizedGrantTypes(c.getAuthorizedGrantTypes())//
                    .redirectUris(c.getRedirectUris())//
                    .additionalInformation(c.getAdditionalInformation())//
                    .authorities(getAuthoritiesByClientId(c.getClientId(), c.getAuthorities()));
            }
        }
    }

    private String[] getAuthoritiesByClientId(String clientId, String[] authorities) {
        if (authorities != null && authorities.length > 0) {
            return authorities;
        }
        Collection<String> authoritiesList = grantedAuthoritiesService.findGrantedAuthorityUrlsByClientId(clientId);
        if (authoritiesList != null) {
            return authoritiesList.stream().toArray(String[]::new);
        }
        return new String[]{};
    }
}