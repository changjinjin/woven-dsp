package com.info.baymax.dsp.job.sch;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring application context holder, will help to perform DI in situations with no spring context available.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    public void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    private static ApplicationContext applicationContext;

    public static <T> T processInjection(T bean) {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        bpp.setBeanFactory(applicationContext.getAutowireCapableBeanFactory());
        bpp.processInjection(bean);
        applicationContext.getAutowireCapableBeanFactory().initializeBean(bean, bean.getClass().getName());
        return bean;
    }

    public static ApplicationContext applicationContext() {
        return applicationContext;
    }
}
