package com.info.baymax.dsp.auth.security.authentication.manager;

import com.google.common.collect.Lists;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.auth.security.authentication.GrantedAuthoritiesService;
import com.info.baymax.dsp.auth.security.config.SecurityInitProperties;
import com.info.baymax.dsp.auth.security.i18n.SecurityMessageSource;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdInfo;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现用户信息加载方法
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:04:49
 */
@Slf4j
@Service
public class ManagerUserDetailsService implements UserDetailsService, GrantedAuthoritiesService {

	@Autowired
	public SecurityInitProperties initProps;

	@Autowired
	public PasswordEncoder passwordEncoder;

	@Autowired
	public TenantService tenantService;

	@Autowired
	public TenantInitializer tenantInitializer;

	@Autowired
	public UserService userService;

	@Autowired
	public PermissionService permissionService;
	@Autowired
	public RestOperationService restOperationService;

	protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();

	private AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 用户名为空
		if (StringUtils.isEmpty(username)) {
			String message = this.messages.getMessage("UserErr.wrongUsername", "Wrong username: null or empty");
			log.debug(message);
			throw new UsernameNotFoundException(message);
		}
		String clientId = null;
		String tenantName = null;
		String userName = null;
		try {
			String[] certificates = username.split(":");
			clientId = certificates[0];
			tenantName = certificates[1];
			userName = certificates[2];
		} catch (Exception e) {
			String message = this.messages.getMessage("UserErr.wrongUsername", "Wrong username: null or empty");
			log.error(message, e);
			throw new UsernameNotFoundException(message);
		}
		// 检查用户信息
		SimpleManagerDetails userDetails = findUserByClientIdAndUsername(clientId, tenantName, userName);
		checker.check(userDetails);

		// 如果密码检查是严格模式，则登录需要检查是否需要修改密码
		userDetails.setPwdInfo(pwdInfo(initProps.isPwdStrict(), userDetails.getUser(), initProps.getPassword()));

		return userDetails;
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
	@Override
	public Collection<? extends GrantedAuthority> findGrantedAuthorities() {
		Collection<String> list = findGrantedAuthorityUrls();
		if (ICollections.hasElements(list)) {
			return list.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
	@Override
	public Collection<String> findGrantedAuthorityUrls() {
		return permissionService.findAllAuthorities();
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
	@Override
	public Collection<String> findGrantedAuthorityUrlsByClientIdAndUsername(String clientId, String tenant,
			String username) {
		Collection<? extends GrantedAuthority> list = findGrantedAuthoritiesByClientIdAndUsername(clientId, tenant,
				username);
		if (ICollections.hasElements(list)) {
			return list.stream().map(t -> t.getAuthority()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
	@Override
	public Collection<? extends GrantedAuthority> findGrantedAuthoritiesByClientIdAndUsername(String clientId,
			String tenant, String username) {
		SimpleManagerDetails userdetails = findUserByClientIdAndUsername(clientId, tenant, username);
		return userdetails.getUser().getAuthorities();
	}

	// 根据客户端ID和用户名查询系统用户信息
	private SimpleManagerDetails findUserByClientIdAndUsername(String clientId, String tenantName, String username) {
		Tenant tenant = tenantService.findByName(tenantName);
		User user = userService.findByTenantAndUsername(tenant.getId(), username);
		// 用户不存在
		if (user == null) {
			String message = this.messages.getMessage("UserErr.usernameNotFound", new Object[] { username },
					"Username {0} not found");
			log.debug(message);
			throw new UsernameNotFoundException(message);
		}

		// 如果是管理员权限则需要用户拥有所有的权限，这里赋给该账户所有的权限
		if (user.admin()) { // 初始获取如果为空则初始化
			List<RestOperation> list = restOperationService.selectList(ExampleQuery.builder(RestOperation.class)
					.fieldGroup(FieldGroup.builder().andEqualTo("enabled", 1)));
			if (ICollections.hasElements(list)) {
				user.setAuthorities(list.stream().map(t -> new SimpleGrantedAuthority(t.operationKey()))
						.collect(Collectors.toList()));
			} else {
				user.setAuthorities(Lists.newArrayList(new SimpleGrantedAuthority("Manager")));
			}
		} else {
			String clientIds = user.getClientIds();
			if (StringUtils.isEmpty(clientIds) || !clientIds.contains(clientId)) {
				throw new NoGrantedAnyAuthorityException("当前用户没有授予平台" + clientId + "的访问权限，禁止登陆该平台！");
			}

			if (ICollections.hasNoElements(user.getAuthorities())) {
				user.setAuthorities(Lists.newArrayList(new SimpleGrantedAuthority("Manager")));
			}
		}
		return new SimpleManagerDetails(clientId, tenant, user);
	}

	/**
	 * <pre>
	 * 是否需要修改密码:
	 *  1.如果是初始密码需要修改
	 *  2.密码是否过期
	 * </pre>
	 *
	 * @return 是否需要修改密码
	 */
	private PwdInfo pwdInfo(boolean isPwdStrict, User user, String initPwd) {
		if (isPwdStrict) {
			return new PwdInfo(PwdMode.STRICT, !user.isCredentialsNonExpired(),
					passwordEncoder.matches(initPwd, user.getPassword()));
		}
		return new PwdInfo(PwdMode.SIMPLE, false, false);
	}
}
