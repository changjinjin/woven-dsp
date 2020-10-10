package com.merce.woven.cas.client.reactive.util;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuthenUtil {
    public static Authentication createAuthen(final String username) {
        final String key = UUID.randomUUID().toString().replaceAll("-", "");
        final List<SimpleGrantedAuthority> c = Arrays.asList(new SimpleGrantedAuthority("role"));
        final UserDetails user = (UserDetails) new User(username, username, c);
        final Assertion assertion = (Assertion) new AssertionImpl(username);
        final Authentication authentication = (Authentication) new CasAuthenticationToken(key, (Object) username,
            (Object) user, c, user, assertion);
        authentication.setAuthenticated(true);
        return authentication;
    }
}
