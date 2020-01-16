package com.info.baymax.dsp.auth.api.config.manager;

import com.info.baymax.dsp.auth.api.config.WhiteListProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CommonAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired
    private WhiteListProperties whiteListProperties;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(whiteListProperties.getAllWhiteList()).permitAll();
    }
}
