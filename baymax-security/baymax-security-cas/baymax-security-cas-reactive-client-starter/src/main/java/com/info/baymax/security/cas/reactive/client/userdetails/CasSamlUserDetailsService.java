package com.info.baymax.security.cas.reactive.client.userdetails;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CasSamlUserDetailsService extends AbstractCasAssertionUserDetailsService {
    private boolean convertToUpperCase;
    private String roleAttribute;
    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";

    public CasSamlUserDetailsService() {
        this.convertToUpperCase = true;
        this.roleAttribute = "roles";
    }

    public CasSamlUserDetailsService(final String roleAttribute) {
        this.convertToUpperCase = true;
        this.roleAttribute = "roles";
        this.roleAttribute = roleAttribute;
    }

    @SuppressWarnings("unchecked")
    public UserDetails loadUserDetails(final Assertion assertion) throws UsernameNotFoundException {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        final AttributePrincipal principal = assertion.getPrincipal();
        final Map<String, Object> attributes = principal.getAttributes();
        final List<String> roles = (List<String>) attributes.get(this.roleAttribute);
        if (roles != null) {
            for (final String role : roles) {
                grantedAuthorities.add((GrantedAuthority) new SimpleGrantedAuthority(
                    this.convertToUpperCase ? role.toUpperCase() : role));
            }
        }
        return new User(principal.getName(), NON_EXISTENT_PASSWORD_VALUE, true, true, true, true, grantedAuthorities);
    }
}
