package com.merce.woven.cas.client.reactive.userdetails;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cas20UserDetailsService extends AbstractCasAssertionUserDetailsService {
    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";
    private static final List<String> defaultRoles;

    protected UserDetails loadUserDetails(final Assertion assertion) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (final String role : Cas20UserDetailsService.defaultRoles) {
            grantedAuthorities.add((GrantedAuthority) new SimpleGrantedAuthority(role));
        }
        final AttributePrincipal principal = assertion.getPrincipal();
        return (UserDetails) new User(principal.getName(), NON_EXISTENT_PASSWORD_VALUE, true, true, true, true,
            grantedAuthorities);
    }

    static {
        defaultRoles = Arrays.asList("system", "everyone");
    }
}
