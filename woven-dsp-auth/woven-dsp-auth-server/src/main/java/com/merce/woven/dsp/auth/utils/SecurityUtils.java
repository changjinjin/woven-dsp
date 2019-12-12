package com.merce.woven.dsp.auth.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.merce.woven.common.utils.ICollections;

/**
 * 说明：Spring security用户Context中信息获取工具类. <br>
 *
 * @author yjw@jusfoun.com
 * @date 2017年9月6日 下午3:49:48
 */
public final class SecurityUtils {

	/**
	 * 说明： 获取当前用户授权信息. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月6日 下午3:50:12
	 * @return 授权信息
	 */
	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 说明： 获取当前用户授权权限. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月6日 下午3:50:12
	 * @return 授权权限列表
	 */
	public static Collection<? extends GrantedAuthority> getCurrentUserGrantedAuthorities() {
		return getCurrentAuthentication().getAuthorities();
	}

	/**
	 * 说明： 获取当前用户授权权限. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月6日 下午3:50:12
	 * @return 授权权限列表
	 */
	public static Collection<String> getCurrentUserAuthorities() {
		Collection<? extends GrantedAuthority> currentUserGrantedAuthorities = getCurrentUserGrantedAuthorities();
		if (ICollections.hasElements(currentUserGrantedAuthorities)) {
			return currentUserGrantedAuthorities.parallelStream().map(t -> t.getAuthority()).collect(Collectors.toSet());
		}
		return null;
	}

	/**
	 * 说明： 获取当前用户名称. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月6日 下午3:50:12
	 * @return 当前用户名称
	 */
	public static String getCurrentUserUsername() {
		Authentication authentication = getCurrentAuthentication();
		String currentUserName = null;
		if (authentication != null) {
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				currentUserName = authentication.getName();
			}
		}
		return currentUserName;
	}

	/**
	 * 说明： 获取当前用户信息. <br>
	 *
	 * @author yjw@jusfoun.com
	 * @date 2017年9月6日 下午3:50:12
	 * @return 当前用户
	 */
	public static Object getCurrentPrincipal() {
		Authentication authentication = getCurrentAuthentication();
		if (authentication == null)
			return null;
		return authentication.getPrincipal();
	}
}
