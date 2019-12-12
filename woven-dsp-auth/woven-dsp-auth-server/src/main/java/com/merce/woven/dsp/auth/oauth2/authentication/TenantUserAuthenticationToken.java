package com.merce.woven.dsp.auth.oauth2.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantUserAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 7444831017334725302L;
	private String clientId;
	private String tenant;
	private String version;

	public TenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
			String version) {
		super(principal, credentials);
		this.clientId = clientId;
		this.tenant = tenant;
		this.version = version;
	}

	public TenantUserAuthenticationToken(Object principal, Object credentials, String clientId, String tenant,
			String version, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.clientId = clientId;
		this.tenant = tenant;
		this.version = version;
	}
}
