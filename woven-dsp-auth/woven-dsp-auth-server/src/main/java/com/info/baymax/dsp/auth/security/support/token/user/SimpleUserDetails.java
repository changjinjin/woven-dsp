package com.info.baymax.dsp.auth.security.support.token.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.info.baymax.common.enums.types.YesNoType;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdInfo;
import com.info.baymax.dsp.data.sys.entity.security.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleUserDetails extends org.springframework.security.core.userdetails.User {
	private static final long serialVersionUID = 8805645346843532535L;

	/**
	 * 租户名称
	 */
	private final String tenant;

	/**
	 * 系统用户信息
	 */
	private final User user;

	/**
	 * 密码信息
	 */
	public PwdInfo pwdInfo;

	public SimpleUserDetails(String tenant, User user) {
		super(user.getLoginId(), user.getPassword(), YesNoType.YES.equalsTo(user.getEnabled()),
				user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
				grantedAuthorities(user.getAuthorities()));
		this.tenant = tenant;
		this.user = user;
	}

	public static List<? extends GrantedAuthority> grantedAuthorities(Collection<String> authorities) {
		if (ICollections.hasNoElements(authorities)) {
			return null;
		}
		return authorities.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList());
	}
}
