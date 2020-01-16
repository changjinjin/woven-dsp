package com.info.baymax.dsp.auth.api.config.manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DefaultAuthorizeConfigManager implements AuthorizeConfigManager, ApplicationContextAware {

    @Autowired
    private List<AuthorizeConfigProvider> providers;

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigProvider provider : providers) {
            provider.config(config);
        }
        config.anyRequest().authenticated();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initStrategies(applicationContext);
    }

    protected void initStrategies(ApplicationContext context) {
        Map<String, AuthorizeConfigProvider> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            AuthorizeConfigProvider.class, true, false);
        this.providers = new ArrayList<AuthorizeConfigProvider>(beans.values());
        AnnotationAwareOrderComparator.sort(this.providers);
    }
}
