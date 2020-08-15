package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.google.common.collect.Lists;
import com.info.baymax.access.dataapi.api.MapEntity;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.parameters.SqlQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.DataReadException;
import com.info.baymax.dsp.access.dataapi.data.StorageConf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DefaultJdbcDbDataReader implements JdbcDbDataReader, ApplicationContextAware {
    private final List<JdbcDbDataReader> readers = Lists.newArrayList();

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, JdbcDbDataReader> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
                JdbcDbDataReader.class, true, false);
        this.readers.addAll(beans.values());
        AnnotationAwareOrderComparator.sort(this.readers);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return true;
    }

    @Override
    public IPage<MapEntity> readBySql(StorageConf conf, SqlQuery query) throws Exception {
        return findSuitableReader(conf).readBySql(conf, query);
    }

    private JdbcDbDataReader findSuitableReader(StorageConf conf) {
        if (conf != null && ICollections.hasElements(readers)) {
            for (JdbcDbDataReader reader : readers) {
                boolean supports = reader.supports(conf);
                log.debug("DataReader:" + reader.getClass().getName() + ", StorageConf:" + conf.getClass().getName()
                        + ", supports:" + supports);
                if (supports) {
                    log.debug("find suitable DataReader '" + reader.getClass().getSimpleName() + "' for StorageConf '"
                            + conf.getClass().getName() + "'.");
                    return reader;
                }
            }
        }
        throw new DataReadException("No suitable data reader for configuration: " + conf);
    }

}
