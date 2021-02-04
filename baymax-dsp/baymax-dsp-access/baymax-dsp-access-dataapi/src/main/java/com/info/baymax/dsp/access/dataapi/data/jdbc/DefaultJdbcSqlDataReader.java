package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.google.common.collect.Lists;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.result.MapEntity;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
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
public class DefaultJdbcSqlDataReader implements JdbcSqlDataReader, ApplicationContextAware {
    private final List<JdbcSqlDataReader> readers = Lists.newArrayList();

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, JdbcSqlDataReader> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            JdbcSqlDataReader.class, true, false);
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

    private JdbcSqlDataReader findSuitableReader(StorageConf conf) {
        if (conf != null && ICollections.hasElements(readers)) {
            for (JdbcSqlDataReader reader : readers) {
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
