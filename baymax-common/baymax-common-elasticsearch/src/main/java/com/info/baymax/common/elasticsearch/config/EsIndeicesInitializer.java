package com.info.baymax.common.elasticsearch.config;

import com.info.baymax.common.core.Initializer;
import com.info.baymax.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 索引创建
 *
 * @author jingwei.yang
 * @date 2021年6月22日 下午2:57:06
 */
@Slf4j
@Component
@EnableConfigurationProperties(value = MybatisElasticsearchJdbcClientProperties.class)
public class EsIndeicesInitializer implements Initializer {
    @Autowired
    private MybatisElasticsearchJdbcClientProperties properties;

    private final String DEFAULT_BASE_PACKAGE = getClass().getPackage().getName();
    private final String RESOURCE_PATTERN = "/**/*.class";

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public void init() {
        if (properties.autoCreateIndex) {
            Map<String, Class<?>> esEntityClasses = getEsEntityClasses();
            if (esEntityClasses != null && !esEntityClasses.isEmpty()) {
                Collection<Class<?>> classes = esEntityClasses.values();
                for (Class<?> c : classes) {
                    createIndex(c);
                }
            }
        }
    }

    private boolean createIndex(Class<?> clazz) {
        boolean create = true;
        IndexOperations indexOps = restTemplate.indexOps(clazz);
        IndexCoordinates indexCoordinates = indexOps.getIndexCoordinates();
        String indexName = indexCoordinates.getIndexName();
        if (!indexOps.exists()) {
            log.info("index " + indexName + " is not exists, create it ====>");
            create = indexOps.create();
        } else {
            log.info("index " + indexName + " already exists, skip ...");
        }
        log.info("index coordinates:" + JsonUtils.toJson(indexCoordinates, true));
        log.debug("index setting:" + JsonUtils.toJson(indexOps.getSettings(), true));
        log.debug("index mapping:" + JsonUtils.toJson(indexOps.getMapping(), true));
        indexOps.putMapping(indexOps.createMapping());
        return create;
    }

    private Map<String, Class<?>> getEsEntityClasses() {
        Map<String, Class<?>> handlerMap = new HashMap<String, Class<?>>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String[] basePackages = properties.basePackages;
            if (basePackages == null || basePackages.length == 0) {
                basePackages = new String[]{DEFAULT_BASE_PACKAGE};
            }

            String pattern = null;
            Resource[] resources = null;
            MetadataReaderFactory readerfactory = null;
            MetadataReader reader = null;
            String classname = null;
            Class<?> clazz = null;
            Document anno = null;
            for (String pkg : basePackages) {
                pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                resources = resourcePatternResolver.getResources(pattern);
                readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                if (resources != null && resources.length > 0) {
                    for (Resource resource : resources) {
                        reader = readerfactory.getMetadataReader(resource);
                        classname = reader.getClassMetadata().getClassName();
                        clazz = Class.forName(classname);
                        anno = clazz.getAnnotation(Document.class);
                        if (anno != null) {
                            handlerMap.put(classname, clazz);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return handlerMap;
    }
}
