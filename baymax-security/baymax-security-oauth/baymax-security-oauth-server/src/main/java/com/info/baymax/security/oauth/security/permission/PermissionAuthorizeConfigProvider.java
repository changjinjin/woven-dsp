package com.info.baymax.security.oauth.security.permission;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.info.baymax.security.oauth.api.config.manager.AuthorizeConfigProvider;

@Order(100)
@Component
public class PermissionAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers("/api/**").access("@permissionService.hasPermission(request,authentication)");
    }
}
