package com.info.baymax.dsp.gateway.config;

import com.info.baymax.dsp.gateway.web.oauth2.introspection.DspOpaqueTokenSpecCustomizer;
import com.info.baymax.dsp.gateway.web.oauth2.method.DspReactiveAuthorizationManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

    @Autowired
    private WhiteListProperties whiteListProperties;

    @Autowired
    private OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @Autowired
    private DspReactiveAuthorizationManager dspReactiveAuthorizationManager;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        // @formatter:off
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .requestCache().disable()
            .authorizeExchange()
            .pathMatchers(whiteListProperties.getAllWhiteList()).permitAll()
            .pathMatchers("/api/**").access(dspReactiveAuthorizationManager)
            .anyExchange().authenticated()
            .and()
            .headers().frameOptions().disable()
            .and()
            .oauth2ResourceServer().bearerTokenConverter(new ServerBearerTokenAuthenticationConverter()).opaqueToken(new DspOpaqueTokenSpecCustomizer(oAuth2ResourceServerProperties));
        //.oauth2ResourceServer().bearerTokenConverter(new ServerBearerTokenAuthenticationConverter()).jwt();
        // @formatter:on
        return http.build();
    }
}
