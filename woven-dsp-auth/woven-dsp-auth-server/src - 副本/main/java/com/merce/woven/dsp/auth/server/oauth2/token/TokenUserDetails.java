package com.jusfoun.services.auth.server.oauth2.token;

import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.jusfoun.services.ops.api.entity.sys.WfSysUser;

/**
 * 说明：自定义UserDetails.<br>
 * 
 * @author yjw
 * @date 2018年12月1日 下午3:29:37
 */
public class TokenUserDetails implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = 4481362320405568438L;

	private final User user;
	private final WfSysUser sysUser;

	public TokenUserDetails(User user, WfSysUser sysUser) {
		this.sysUser = sysUser;
		this.user = user;
	}

	@Override
	public void eraseCredentials() {
		user.eraseCredentials();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return user.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.isAccountNonExpired();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return user.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public User getUser() {
		return user;
	}

	public WfSysUser getSysUser() {
		return sysUser;
	}

}
