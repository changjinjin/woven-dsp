package com.info.baymax.security.oauth.security.authentication.manager;

import com.google.common.collect.Lists;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.validation.config.PassayProperties;
import com.info.baymax.common.validation.passay.PwdInfo;
import com.info.baymax.common.validation.passay.PwdInfo.PwdMode;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.RestOperationService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
import com.info.baymax.security.oauth.i18n.SecurityMessageSource;
import com.info.baymax.security.oauth.security.authentication.GrantedAuthoritiesService;
import com.info.baymax.security.oauth.security.config.SecurityInitProperties;
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
 * ??????????????????????????????
 *
 * @author: jingwei.yang
 * @date: 2019???4???22??? ??????2:04:49
 */
@Slf4j
@Service
public class ManagerUserDetailsService implements UserDetailsService, GrantedAuthoritiesService {

	@Autowired
	public SecurityInitProperties initProps;
    @Autowired
    public PassayProperties passayProperties;

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
		// ???????????????
		if (StringUtils.isEmpty(username)) {
            String message = this.messages.getMessage("USER_WRONG_USERNAME", "Wrong username: null or empty");
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
            String message = this.messages.getMessage("USER_WRONG_USERNAME", "Wrong username: null or empty");
			log.error(message, e);
			throw new UsernameNotFoundException(message);
		}
		// ??????????????????
		SimpleManagerDetails userDetails = findUserByClientIdAndUsername(clientId, tenantName, userName);
		checker.check(userDetails);

		// ?????????????????????????????????????????????????????????????????????????????????
        userDetails.setPwdInfo(pwdInfo(passayProperties.isStrict(), userDetails.getUser(), initProps.getPassword()));

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

	// ???????????????ID????????????????????????????????????
	private SimpleManagerDetails findUserByClientIdAndUsername(String clientId, String tenantName, String username) {
		Tenant tenant = tenantService.findByName(tenantName);
		User user = userService.findByTenantAndUsername(tenant.getId(), username);
		// ???????????????
		if (user == null) {
            String message = this.messages.getMessage("USER_USERNAME_NOT_FOUND", new Object[]{username},
					"Username {0} not found");
			log.debug(message);
			throw new UsernameNotFoundException(message);
		}

		// ???????????????????????????????????????????????????????????????????????????????????????????????????
		if (user.admin()) { // ????????????????????????????????????
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
				throw new NoGrantedAnyAuthorityException("??????????????????????????????" + clientId + "??????????????????????????????????????????");
			}

			if (ICollections.hasNoElements(user.getAuthorities())) {
				user.setAuthorities(Lists.newArrayList(new SimpleGrantedAuthority("Manager")));
			}
		}
		return new SimpleManagerDetails(clientId, tenant, user);
	}

	/**
	 * <pre>
	 * ????????????????????????:
	 *  1.?????????????????????????????????
	 *  2.??????????????????
	 * </pre>
	 *
	 * @return ????????????????????????
	 */
	private PwdInfo pwdInfo(boolean isPwdStrict, User user, String initPwd) {
		if (isPwdStrict) {
			return new PwdInfo(PwdMode.STRICT, !user.isCredentialsNonExpired(),
					passwordEncoder.matches(initPwd, user.getPassword()));
		}
		return new PwdInfo(PwdMode.SIMPLE, false, false);
	}
}
