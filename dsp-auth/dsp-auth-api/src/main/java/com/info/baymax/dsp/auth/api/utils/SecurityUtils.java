package com.info.baymax.dsp.auth.api.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.auth.api.UserInfo;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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

    // get SaasContext from Authentication
    @SuppressWarnings("unchecked")
	public static SaasContext getSaasContext() {
        Authentication authentication = SecurityUtils.getCurrentAuthentication();
        if (authentication == null) {
            return null;
        }
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oauth = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oauth.getUserAuthentication();
        if (userAuthentication instanceof UserInfo) {
            return getSaasContextFromToken((UserInfo) userAuthentication);
        } else {
            Map<String, Object> details = (Map<String, Object>) userAuthentication.getDetails();
            if (details == null) {
                return null;
            }
            Map<String, Object> userAuthenticationMap = (Map<String, Object>) details.get("userAuthentication");
            if (userAuthenticationMap == null) {
                return null;
            }
            return getSaasContextFromMap(userAuthenticationMap);
        }
    }

    public static SaasContext getSaasContextFromMap(Map<String, Object> userAuthMap) {
        if (userAuthMap == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(userAuthMap));
        SaasContext saasContext = SaasContext.getCurrentSaasContext();
        saasContext.setClientId(json.getString("clientId"));
        saasContext.setTenantName(json.getString("tenant"));
        saasContext.setTenantId(json.getString("tenantId"));
        saasContext.setUserId(json.getString("userId"));
        saasContext.setUsername(json.getString("name"));
        saasContext.setAdmin(json.getBooleanValue("admin"));
        saasContext.setUserType(json.getString("userType"));
        return saasContext;
    }

    public static SaasContext getSaasContextFromToken(UserInfo token) {
        if (token == null) {
            return null;
        }
        SaasContext saasContext = SaasContext.getCurrentSaasContext();
        saasContext.setClientId(token.getClientId());
        saasContext.setTenantName(token.getTenantName());
        saasContext.setTenantId(token.getTenantId());
        saasContext.setUserId(token.getUserId());
        saasContext.setUsername(token.getUserName());
        saasContext.setAdmin(token.isAdmin());
        saasContext.setUserType(token.getUserType().name());
        return saasContext;
    }
}
