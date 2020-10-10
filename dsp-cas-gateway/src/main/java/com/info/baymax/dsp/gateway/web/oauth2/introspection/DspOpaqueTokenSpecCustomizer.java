package com.info.baymax.dsp.gateway.web.oauth2.introspection;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.OpaqueTokenSpec;

public class DspOpaqueTokenSpecCustomizer implements Customizer<OpaqueTokenSpec> {

	private final OAuth2ResourceServerProperties properties;

	public DspOpaqueTokenSpecCustomizer(final OAuth2ResourceServerProperties properties) {
		this.properties = properties;
	}

	@Override
	public void customize(OpaqueTokenSpec t) {
		t.introspector(new DspReactiveOpaqueTokenIntrospector(properties.getOpaquetoken().getIntrospectionUri()));
	}

}
