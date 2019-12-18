package com.info.baymax.dsp.auth.oauth2.authentication;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.info.baymax.dsp.auth.security.exceptions.ClientIdNotFoundException;
import com.info.baymax.dsp.auth.security.i18n.SecurityMessageSource;
import com.info.baymax.dsp.auth.security.support.tenant.TenantDetails;
import com.info.baymax.dsp.auth.security.support.tenant.TenantDetailsService;
import com.info.baymax.dsp.auth.security.support.token.user.SimpleUserDetails;

public class TenantUserAuthenticationProvider implements AuthenticationProvider {
    protected final MessageSourceAccessor messages = SecurityMessageSource.getAccessor();
    private final TenantDetailsService tenantDetailsService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TenantUserAuthenticationProvider(final TenantDetailsService tenantDetailsService,
                                            final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
        this.tenantDetailsService = tenantDetailsService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        }
        // 先校验租户信息，再校验用户信息
        TenantUserAuthenticationToken token = (TenantUserAuthenticationToken) authentication;

        TenantDetails<?> tenant = tenantDetailsService.findByTenant(token.getTenant());
        if (tenant == null) {
            throw new ClientIdNotFoundException(this.messages.getMessage("ClientErr.clientNotFound",
                new Object[]{token.getTenant()}, "Client {0} not found !"));
        }

        final String username = authentication.getName();
        final String presentedPassword = (String) authentication.getCredentials();
        SimpleUserDetails userDetails = (SimpleUserDetails) this.userDetailsService//
            .loadUserByUsername(token.getClientId() + ":" + token.getTenant() + ":" + username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(this.messages.getMessage("UserErr.usernameNotFound",
                new Object[]{username}, "User {0} not found !"));
        }

        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException(this.messages.getMessage("UserErr.badCredentials", "Bad Credentials !"));
        }

        return new TenantUserAuthenticationToken(token.getPrincipal(), token.getCredentials(), token.getClientId(),
            token.getTenant(), token.getVersion(), userDetails.getTenant().getId(), userDetails.getUser().getId(),
            userDetails.getUser().admin(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (TenantUserAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
