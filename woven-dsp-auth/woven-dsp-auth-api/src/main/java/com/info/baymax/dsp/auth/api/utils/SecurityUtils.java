package com.info.baymax.dsp.auth.api.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Collection<? extends GrantedAuthority> getCurrentUserGrantedAuthorities() {
		return getCurrentAuthentication().getAuthorities();
	}

	public static Collection<String> getCurrentUserAuthorities() {
		Collection<? extends GrantedAuthority> currentUserGrantedAuthorities = getCurrentUserGrantedAuthorities();
		if (currentUserGrantedAuthorities != null && !currentUserGrantedAuthorities.isEmpty()) {
			return currentUserGrantedAuthorities.parallelStream().map(t -> t.getAuthority())
					.collect(Collectors.toSet());
		}
		return null;
	}

	public static String getCurrentUserUsername() {
		Authentication authentication = getCurrentAuthentication();
		String currentUserName = null;
		if (authentication != null) {
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
				currentUserName = authentication.getName();
			}
		}
		return currentUserName;
	}

	public static Object getCurrentPrincipal() {
		Authentication authentication = getCurrentAuthentication();
		if (authentication == null)
			return null;
		return authentication.getPrincipal();
	}
}
