package com.merce.woven.dsp.auth.server.oauth2;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.jusfoun.services.ops.api.entity.sys.WfSysUser;
import com.jusfoun.services.ops.api.enums.AccountStatus;
import com.jusfoun.services.ops.client.WfSysPermissionClientService;
import com.jusfoun.services.ops.client.WfSysUserClientService;
import com.merce.woven.common.utils.ICollections;
import com.merce.woven.dsp.auth.server.oauth2.token.TokenUserDetails;

/**
 * 说明： 实现用户信息加载方法. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月14日 下午1:43:11
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private WfSysUserClientService wfSysUserClientService;

	@Autowired
	private WfSysPermissionClientService wfSysPermissionClientService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		// 用户名为空
		if (StringUtils.isEmpty(username)) {
			throw new UsernameNotFoundException("Parameter 'username' cound not be null or empty !");
		}

		// 查询用户信息
		/*
		 * SysUser sysUser = new SysUser(); sysUser.setUsername(username);
		 * BaseResponse<SysUser> info = sysUserClientService.info(sysUser); if
		 * (info.ok()) { sysUser = info.getContent(); } else { sysUser = null; }
		 */

		WfSysUser sysUser = wfSysUserClientService.findByUsername(username);

		// 用户不存在
		if (sysUser == null) {
			throw new UsernameNotFoundException(String.format("User not found with username '%s' !", username));
		}

		// 账户锁定
		if (AccountStatus.LOCKED.equalsTo(sysUser.getStatus())) {
			throw new LockedException(String.format("Locked account '%s' !", username));
		}

		// 账户失效
		if (AccountStatus.DISABLED.equalsTo(sysUser.getStatus())
				|| AccountStatus.NOT_ENABLED.equalsTo(sysUser.getStatus())) {
			throw new DisabledException(String.format("Disabled account '%s' !", username));
		}

		// 如果是管理员权限则需要用户拥有所有的权限，这里赋给该账户所有的权限
		if (sysUser.getIsAdmin()) {
			sysUser.setAuthorities(wfSysPermissionClientService.findAllPermissions().parallelStream()
					.map(t -> t.getPermUrl()).collect(Collectors.toSet()));
		}

		// 用户没有赋权，用户需要有权限才能登陆服务
		Set<String> authorities = sysUser.getAuthorities();
		// if (ICollections.hasNoElements(authorities)) {
		// sthrow new AuthenticationServiceException("User has no authority
		// assigned !");
		// }

		// 返回带有用户权限信息的User
		List<SimpleGrantedAuthority> grantedAuthorities = null;
		if (ICollections.hasElements(authorities)) {
			grantedAuthorities = authorities.stream().map(t -> new SimpleGrantedAuthority(t))
					.collect(Collectors.toList());
		}
		User user = new User(sysUser.getUsername(), sysUser.getPassword(),
				AccountStatus.ENABLED.equalsTo(sysUser.getStatus()), true, true, true, grantedAuthorities);
		return new TokenUserDetails(user, sysUser);
	}
}
