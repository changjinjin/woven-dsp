package com.info.baymax.dsp.common.swagger.handle;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import springfox.documentation.PathProvider;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.service.RequestHandlerCombiner;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.AbstractDocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.plugins.SpringIntegrationPluginNotPresentInClassPathCondition;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 当DocumentationPluginsBootstrapper类完成swagger相关的扫描之后开始初始化Swagger对象
 *
 * @author jingwei.yang
 * @date 2020年4月23日 下午7:39:34
 */
@Slf4j
@Component
@Conditional(SpringIntegrationPluginNotPresentInClassPathCondition.class)
public class Swagger2HandlerBootstrapper extends AbstractDocumentationPluginsBootstrapper
    implements SmartLifecycle, ApplicationContextAware {

    @Value("${swagger2.handler.auto-startup:true}")
    private boolean autoStartup;
    @Value("${swagger2.handler.auto-execute:false}")
    private boolean autoExecute;

    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;

    private List<SwaggerHandler> handlers;
    private AtomicBoolean initialized = new AtomicBoolean(false);

    @Autowired
    public Swagger2HandlerBootstrapper(DocumentationPluginsManager documentationPluginsManager,
                                       List<RequestHandlerProvider> handlerProviders, DocumentationCache scanned,
                                       ApiDocumentationScanner resourceListing, TypeResolver typeResolver, Defaults defaults,
                                       PathProvider pathProvider, DocumentationCache documentationCache, ServiceModelToSwagger2Mapper mapper) {
        super(documentationPluginsManager, handlerProviders, scanned, resourceListing, defaults, typeResolver,
            pathProvider);
        this.documentationCache = documentationCache;
        this.mapper = mapper;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, SwaggerHandler> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            SwaggerHandler.class, true, false);
        this.handlers = new ArrayList<SwaggerHandler>(beans.values());
        AnnotationAwareOrderComparator.sort(this.handlers);
    }

    @Override
    public boolean isAutoStartup() {
        return autoStartup;
    }

    @Override
    public void stop(Runnable callback) {
        callback.run();
    }

    @Override
    public void stop() {
        initialized.getAndSet(false);
        getScanned().clear();
    }

    @Override
    public boolean isRunning() {
        return initialized.get();
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    @Autowired(required = false)
    public void setCombiner(RequestHandlerCombiner combiner) {
        super.setCombiner(combiner);
    }

    @Override
    @Autowired(required = false)
    public void setTypeConventions(List<AlternateTypeRuleConvention> typeConventions) {
        super.setTypeConventions(typeConventions);
    }

    @Override
    public void start() {
        if (initialized.compareAndSet(false, true)) {
            log.info("Documentation plugins bootstrapped");
            super.bootstrapDocumentationPlugins();
        }

        if (!autoExecute) {
            log.debug("skip swagger handlers auto execute.");
            return;
        }

        Swagger swagger = swagger();
        if (swagger == null) {
            log.debug("no swagger to be handle.");
            return;
        }

        if (handlers == null || handlers.isEmpty()) {
            log.debug("no SwaggerHandler to handle swagger.");
            return;
        }

        for (SwaggerHandler handler : handlers) {
            log.debug("SwaggerHandler {} handle swagger.", handler.getClass().getName());
            handler.handle(swagger);
        }
    }

    public Swagger swagger() {
        String groupName = Docket.DEFAULT_GROUP_NAME;
        Documentation documentation = documentationCache.documentationByGroup(groupName);
        if (documentation == null) {
            log.warn("Unable to find specification for group {}", groupName);
            throw new RuntimeException(String.format("Unable to find specification for group %s", groupName));
        }
        Swagger swagger = mapper.mapDocumentation(documentation);
        swagger.basePath("/");
        return swagger;
    }
}
