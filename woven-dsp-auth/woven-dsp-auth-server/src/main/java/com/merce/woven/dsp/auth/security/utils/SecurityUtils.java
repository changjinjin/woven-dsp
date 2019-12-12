package com.merce.woven.dsp.auth.security.utils;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.merce.woven.common.utils.ICollections;

public final class SecurityUtils {

	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Collection<? extends GrantedAuthority> getCurrentUserGrantedAuthorities() {
		return getCurrentAuthentication().getAuthorities();
	}

	public static Collection<String> getCurrentUserAuthorities() {
		Collection<? extends GrantedAuthority> currentUserGrantedAuthorities = getCurrentUserGrantedAuthorities();
		if (ICollections.hasElements(currentUserGrantedAuthorities)) {
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
