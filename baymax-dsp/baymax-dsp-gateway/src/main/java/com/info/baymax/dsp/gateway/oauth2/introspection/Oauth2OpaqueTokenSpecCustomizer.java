package com.info.baymax.dsp.gateway.oauth2.introspection;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec;

public class Oauth2OpaqueTokenSpecCustomizer implements Customizer<OpaqueTokenSpec> {

    private final OAuth2ResourceServerProperties properties;

    public Oauth2OpaqueTokenSpecCustomizer(final OAuth2ResourceServerProperties properties) {
        this.properties = properties;
    }

    @Override
    public void customize(OpaqueTokenSpec t) {
        t.introspector(new Oauth2ReactiveOpaqueTokenIntrospector(properties.getOpaquetoken().getIntrospectionUri()));
    }

}
