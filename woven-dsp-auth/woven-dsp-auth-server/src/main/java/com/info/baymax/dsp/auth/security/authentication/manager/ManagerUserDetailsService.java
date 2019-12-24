package com.info.baymax.dsp.auth.security.authentication.manager;

import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.auth.security.authentication.GrantedAuthoritiesService;
import com.info.baymax.dsp.auth.security.config.SecurityInitConfig;
import com.info.baymax.dsp.auth.security.i18n.SecurityMessageSource;
import com.info.baymax.dsp.data.sys.constant.CacheNames;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdInfo;
import com.info.baymax.dsp.data.sys.crypto.pwd.PwdMode;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.entity.security.User;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;
import com.info.baymax.dsp.data.sys.service.security.PermissionService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.dsp.data.sys.service.security.UserService;
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
@Service
public class ManagerUserDetailsService implements UserDetailsService, GrantedAuthoritiesService {

    @Autowired
    public SecurityInitConfig initConfig;

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

    protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();

    private AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 用户名为空
        if (StringUtils.isEmpty(username)) {
            throw new UsernameNotFoundException(
                this.messages.getMessage("UserErr.wrongUsername", "Wrong username: null or empty"));
        }
        String[] certificates = username.split(":");
        // 检查用户信息
        SimpleManagerDetails userDetails = findUserByClientIdAndUsername(certificates[0], certificates[1],
            certificates[2]);
        checker.check(userDetails);

        // 如果密码检查是严格模式，则登录需要检查是否需要修改密码
        userDetails.setPwdInfo(pwdInfo(initConfig.isPwdStrict(), userDetails.getUser(), initConfig.getPassword()));

        return userDetails;
    }

    @Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
    @Override
    public Collection<? extends GrantedAuthority> findGrantedAuthoritiesByClientId(String clientId) {
        Collection<String> list = findGrantedAuthorityUrlsByClientId(clientId);
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Cacheable(cacheNames = CacheNames.CACHE_SECURITY, unless = "#result == null")
    @Override
    public Collection<String> findGrantedAuthorityUrlsByClientId(String clientId) {
        return permissionService.findAuthoritiesByClientId(clientId);
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
            throw new UsernameNotFoundException(this.messages.getMessage("UserErr.usernameNotFound",
                new Object[]{username}, "Username {0} not found"));
        }

        // 如果是管理员权限则需要用户拥有所有的权限，这里赋给该账户所有的权限

        if (user.admin()) { // 初始获取如果为空则初始化
            List<String> list = permissionService.findAuthoritiesByClientId(clientId);
            if (ICollections.hasNoElements(list)) {
                throw new NoGrantedAnyAuthorityException(this.messages.getMessage("UserErr.noAuthorityGranted",
                    new Object[]{username}, "Username {0} no authority granted"));
            }
            user.setAuthorities(list.stream().map(t -> new SimpleGrantedAuthority(t)).collect(Collectors.toList()));
        }

        // 用户没有赋权，用户需要有权限才能登陆服务
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            throw new NoGrantedAnyAuthorityException(this.messages.getMessage("UserErr.noAuthorityGranted",
                new Object[]{username}, "Username {0} no authority granted"));
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
