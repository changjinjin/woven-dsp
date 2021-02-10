package com.info.baymax.common.config.init;

import com.info.baymax.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InitializersRunner implements CommandLineRunner, ApplicationContextAware {

    private List<Initializer> initializers = new ArrayList<Initializer>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, Initializer> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,
            Initializer.class, true, false);
        this.initializers = new ArrayList<Initializer>(beans.values());
        AnnotationAwareOrderComparator.sort(this.initializers);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            log.debug("start execute the initializers...");
            if (ICollections.hasElements(initializers)) {
                for (Initializer initializer : initializers) {
                    initializer.init();
                }
            }
            log.debug("end execute the initializers...");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
