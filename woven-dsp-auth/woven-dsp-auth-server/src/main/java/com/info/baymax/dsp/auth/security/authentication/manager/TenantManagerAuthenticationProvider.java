package com.info.baymax.dsp.auth.security.authentication.manager;

import com.info.baymax.dsp.auth.security.authentication.tenant.TenantDetails;
import com.info.baymax.dsp.auth.security.authentication.tenant.TenantDetailsService;
import com.info.baymax.dsp.auth.security.authentication.tenant.TenantNotFoundException;
import com.info.baymax.dsp.auth.security.i18n.SecurityMessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TenantManagerAuthenticationProvider implements AuthenticationProvider {
    protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();
    private final TenantDetailsService tenantDetailsService;
    private final ManagerUserDetailsService managerUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TenantManagerAuthenticationProvider(final TenantDetailsService tenantDetailsService,
                                               final ManagerUserDetailsService managerUserDetailsService, final PasswordEncoder passwordEncoder) {
        this.tenantDetailsService = tenantDetailsService;
        this.managerUserDetailsService = managerUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        }
        // 先校验租户信息，再校验用户信息
        TenantManagerAuthenticationToken token = (TenantManagerAuthenticationToken) authentication;

        TenantDetails<?> tenant = tenantDetailsService.findByTenant(token.getTenant());
        if (tenant == null) {
            throw new TenantNotFoundException(this.messages.getMessage("ClientErr.clientNotFound",
                new Object[]{token.getTenant()}, "Client {0} not found !"));
        }

        final String username = authentication.getName();
        final String presentedPassword = (String) authentication.getCredentials();
        SimpleManagerDetails userDetails = (SimpleManagerDetails) this.managerUserDetailsService
            .loadUserByUsername(token.getClientId() + ":" + token.getTenant() + ":" + username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(this.messages.getMessage("UserErr.usernameNotFound",
                new Object[]{username}, "User {0} not found !"));
        }

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(this.messages.getMessage("UserErr.badCredentials", "Bad Credentials !"));
        }

        return new TenantManagerAuthenticationToken(token.getPrincipal(), token.getCredentials(), token.getClientId(),
            token.getTenant(), token.getVersion(), userDetails.getTenant().getId(), userDetails.getUser().getId(),
            userDetails.getUser().admin(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (TenantManagerAuthenticationToken.class.isAssignableFrom(authentication)
            || (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication)));
    }
}
