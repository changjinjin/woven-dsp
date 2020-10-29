package com.info.baymax.security.oauth.security.authentication.customer;

import com.info.baymax.dsp.data.sys.entity.security.Customer;
import com.info.baymax.dsp.data.sys.entity.security.Tenant;
import com.info.baymax.dsp.data.sys.service.security.CustomerService;
import com.info.baymax.dsp.data.sys.service.security.TenantService;
import com.info.baymax.security.oauth.security.i18n.SecurityMessageSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 实现用户信息加载方法
 *
 * @author: jingwei.yang
 * @date: 2019年4月22日 下午2:04:49
 */
@Slf4j
@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    public TenantService tenantService;
    @Autowired
    public CustomerService customerService;

    protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();
    private AccountStatusUserDetailsChecker checker = new AccountStatusUserDetailsChecker();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
            throw new UsernameNotFoundException(message, e);
        }
        SimpleCustomerDetails userDetails = findUserByClientIdAndUsername(clientId, tenantName, userName);
        checker.check(userDetails);
        return userDetails;
    }

    private SimpleCustomerDetails findUserByClientIdAndUsername(String clientId, String tenantName, String username) {
        Tenant tenant = tenantService.findByName(tenantName);
        Customer user = customerService.findByTenantAndUsername(tenant.getId(), username);
        if (user == null) {
            String message = this.messages.getMessage("UserErr.usernameNotFound", new Object[]{username},
                "Username {0} not found");
            log.debug(message);
            throw new UsernameNotFoundException(message);
        }
        return new SimpleCustomerDetails(clientId, tenant, user);
    }
}
