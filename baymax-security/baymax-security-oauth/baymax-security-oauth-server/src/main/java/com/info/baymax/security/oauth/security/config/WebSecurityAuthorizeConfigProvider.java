package com.info.baymax.security.oauth.security.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.info.baymax.dsp.data.sys.constant.AuthConstants;
import com.info.baymax.security.oauth.api.config.manager.AuthorizeConfigProvider;

@Order(1)
@Component
public class WebSecurityAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config//
            .antMatchers(AuthConstants.TOKEN_ENTRY_POINT).fullyAuthenticated()//
            .antMatchers(AuthConstants.TOKEN_REFRESH_ENTRY_POINT).permitAll()//
        ;
    }

}
