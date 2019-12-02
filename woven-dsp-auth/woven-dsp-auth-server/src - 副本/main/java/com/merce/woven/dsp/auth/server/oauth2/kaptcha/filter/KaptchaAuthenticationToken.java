package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Kaptcha token包装对象
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:30:53
 */
public class KaptchaAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 2877954820905567501L;

	private final Object principal;
	private Object credentials;
	private String uuid;
	private String imageCode;

	public KaptchaAuthenticationToken(Object principal, Object credentials, String uuid, String imageCode) {
		this(principal, credentials, uuid, imageCode, null);
	}

	public KaptchaAuthenticationToken(Object principal, Object credentials, String uuid, String imageCode,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.uuid = uuid;
		this.imageCode = imageCode;
		this.setAuthenticated(false);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public Object getCredentials() {
		return this.credentials;
	}

	public Object getPrincipal() {
		return this.principal;
	}

	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		credentials = null;
	}

}
