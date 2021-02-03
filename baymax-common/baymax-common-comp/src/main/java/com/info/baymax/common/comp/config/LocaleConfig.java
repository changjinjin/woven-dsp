package com.info.baymax.common.comp.config;

import com.info.baymax.common.core.i18n.MessageSourceLocator;
import com.info.baymax.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.*;

@Configuration
public class LocaleConfig implements ApplicationContextAware {
    private List<MessageSourceLocator> locators = new ArrayList<MessageSourceLocator>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, MessageSourceLocator> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
            MessageSourceLocator.class, true, false);
        this.locators = new ArrayList<MessageSourceLocator>(beans.values());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public MessageSourceProperties externalMessageSourceProperties() {
        return new MessageSourceProperties();
    }

    @Primary
    @Bean
    public MessageSourceProperties messageSourceProperties(MessageSourceProperties externalMessageSourceProperties) {
        String basename = "";
        if (ICollections.hasElements(locators)) {
            Set<String> basenames = new HashSet<String>();
            for (MessageSourceLocator locator : locators) {
                basenames.addAll(locator.baseNames());
            }
            basename = StringUtils.join(basenames, ",");
        }
        if (externalMessageSourceProperties != null
            && StringUtils.isNotEmpty(externalMessageSourceProperties.getBasename())) {
            basename = StringUtils.isEmpty(basename) ? externalMessageSourceProperties.getBasename()
                : basename + "," + externalMessageSourceProperties.getBasename();
        }

        MessageSourceProperties messageSourceProperties = new MessageSourceProperties();
        messageSourceProperties.setBasename(basename);
        return messageSourceProperties;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(final MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource, Locale.getDefault());
    }
}
