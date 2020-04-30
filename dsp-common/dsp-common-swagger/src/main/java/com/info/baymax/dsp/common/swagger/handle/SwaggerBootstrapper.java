package com.info.baymax.dsp.common.swagger.handle;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.models.Swagger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springfox.documentation.PathProvider;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.service.RequestHandlerProvider;
import springfox.documentation.spi.service.contexts.Defaults;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.plugins.SpringIntegrationPluginNotPresentInClassPathCondition;
import springfox.documentation.spring.web.scanners.ApiDocumentationScanner;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 当DocumentationPluginsBootstrapper类完成swagger相关的扫描之后开始初始化Swagger对象
 *
 * @author jingwei.yang
 * @date 2020年4月23日 下午7:39:34
 */
@Slf4j
@Component
@Conditional(SpringIntegrationPluginNotPresentInClassPathCondition.class)
public class SwaggerBootstrapper extends DocumentationPluginsBootstrapper implements ApplicationContextAware {

    private final DocumentationCache documentationCache;
    private final ServiceModelToSwagger2Mapper mapper;

    private List<SwaggerHandler> handlers;

    @Autowired
    public SwaggerBootstrapper(DocumentationPluginsManager documentationPluginsManager,
                               List<RequestHandlerProvider> handlerProviders, DocumentationCache scanned,
                               ApiDocumentationScanner resourceListing, TypeResolver typeResolver, Defaults defaults,
                               PathProvider pathProvider, Environment environment, DocumentationCache documentationCache,
                               ServiceModelToSwagger2Mapper mapper) {
        super(documentationPluginsManager, handlerProviders, scanned, resourceListing, typeResolver, defaults,
            pathProvider, environment);
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
    public void start() {
        super.start();
        if (handlers == null || handlers.isEmpty()) {
            log.debug("no SwaggerHandler to handle swagger.");
            return;
        }
        Swagger swagger = swagger();
        if (swagger == null) {
            log.debug("no swagger to be handle.");
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
