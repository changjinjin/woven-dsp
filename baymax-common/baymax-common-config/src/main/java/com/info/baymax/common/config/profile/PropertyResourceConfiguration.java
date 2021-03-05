package com.info.baymax.common.config.profile;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 自定义配置文件加引入配置自动装配器，使用EnableExtProperties注解可使用统配符的方式指定需要加载的配置文件
 *
 * @author jingwei.yang
 * @date 2019年12月25日 下午4:22:59
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean(annotation = {EnableExtProperties.class})
public class PropertyResourceConfiguration implements ImportAware {

    @Nullable
    private AnnotationAttributes enableEEP;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableEEP = fromMap(importMetadata.getAnnotationAttributes(EnableExtProperties.class.getName()));
        if (this.enableEEP == null) {
            throw new IllegalArgumentException(
                "@EnableExtProperty is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        if (this.enableEEP != null) {
            String[] value = enableEEP.getStringArray("value");
            if (value != null && value.length > 0) {
                List<Resource> locations = new ArrayList<Resource>();
                PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
                for (String pathPattern : value) {
                    Resource[] resources = resourceResolver.getResources(pathPattern);
                    locations.addAll(Arrays.asList(resources));
                }
                ppc.setLocations(locations.stream().toArray(Resource[]::new));
            }
        }
        return ppc;
    }

    @Nullable
    private AnnotationAttributes fromMap(@Nullable Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        if (map instanceof AnnotationAttributes) {
            return (AnnotationAttributes) map;
        }
        return new AnnotationAttributes(map);
    }
}
