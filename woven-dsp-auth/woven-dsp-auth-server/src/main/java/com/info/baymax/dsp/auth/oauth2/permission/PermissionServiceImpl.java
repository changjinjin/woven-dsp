package com.info.baymax.dsp.auth.oauth2.permission;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.dsp.auth.api.utils.SecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private ClientDetailsService clientDetailsService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        String requestUrl = request.getRequestURI();
        SaasContext saasContext = SecurityUtils.getSaasContext();
        if (saasContext != null) {

            // 如果是超级管理员直接放行
            if (saasContext.isAdmin()) {
                return true;
            }

            // 判断客户端权限
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(saasContext.getClientId());
            if (clientDetails == null) {
                throw new AccessDeniedException("Invalid request client!");
            }

            // 如果该client没有需要鉴权的权限则放行
            Collection<GrantedAuthority> clientGrantedAuthorities = clientDetails.getAuthorities();
            if (clientGrantedAuthorities == null || clientGrantedAuthorities.isEmpty()) {
                return true;
            }

            // 是否包含该路径，包含则需要鉴权，否则不需要
            Set<String> perms = new HashSet<String>();
            if (clientGrantedAuthorities != null && clientGrantedAuthorities.size() > 0) {
                perms = clientGrantedAuthorities.stream().map(t -> t.getAuthority()).collect(Collectors.toSet());
            }
            if (!perms.contains(requestUrl)) {
                return true;
            }

            // 如果包含路径则判断用户是否有该权限
            Collection<? extends GrantedAuthority> userGrantedAuthorities = authentication.getAuthorities();
            for (GrantedAuthority authority : userGrantedAuthorities) {
                if (antPathMatcher.match(authority.getAuthority(), requestUrl)) {
                    return true;
                }
            }
        }
        throw new AccessDeniedException("Access Denied:" + requestUrl);
    }
}
