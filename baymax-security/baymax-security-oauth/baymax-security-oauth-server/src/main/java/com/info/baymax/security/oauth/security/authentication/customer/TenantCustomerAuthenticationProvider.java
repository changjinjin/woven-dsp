package com.info.baymax.security.oauth.security.authentication.customer;

import com.info.baymax.security.oauth.i18n.SecurityMessageSource;
import com.info.baymax.security.oauth.security.authentication.tenant.TenantDetails;
import com.info.baymax.security.oauth.security.authentication.tenant.TenantDetailsService;
import com.info.baymax.security.oauth.security.authentication.tenant.TenantNotFoundException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TenantCustomerAuthenticationProvider implements AuthenticationProvider {
    protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();
    private final TenantDetailsService tenantDetailsService;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TenantCustomerAuthenticationProvider(final TenantDetailsService tenantDetailsService,
                                                final CustomerUserDetailsService customerUserDetailsService, final PasswordEncoder passwordEncoder) {
        this.tenantDetailsService = tenantDetailsService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        }
        // 先校验租户信息，再校验用户信息
        TenantCustomerAuthenticationToken token = (TenantCustomerAuthenticationToken) authentication;

        TenantDetails<?> tenant = tenantDetailsService.findByTenant(token.getTenant());
        if (tenant == null) {
            throw new TenantNotFoundException(this.messages.getMessage("ClientErr.clientNotFound",
                new Object[]{token.getTenant()}, "Client {0} not found !"));
        }

        final String username = authentication.getName();
        final String presentedPassword = (String) authentication.getCredentials();
        SimpleCustomerDetails userDetails = (SimpleCustomerDetails) this.customerUserDetailsService//
            .loadUserByUsername(token.getClientId() + ":" + token.getTenant() + ":" + username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(this.messages.getMessage("UserErr.usernameNotFound",
                new Object[]{username}, "User {0} not found !"));
        }

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(this.messages.getMessage("UserErr.badCredentials", "Bad Credentials !"));
        }

        return new TenantCustomerAuthenticationToken(token.getPrincipal(), token.getCredentials(), token.getClientId(),
            token.getTenant(), token.getVersion(), userDetails.getTenant().getId(), userDetails.getUser().getId(),
            false, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (TenantCustomerAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
