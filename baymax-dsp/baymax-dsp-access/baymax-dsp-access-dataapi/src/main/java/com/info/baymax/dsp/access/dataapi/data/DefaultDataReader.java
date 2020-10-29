package com.info.baymax.dsp.access.dataapi.data;

import com.google.common.collect.Lists;
import com.info.baymax.common.queryapi.page.IPage;
import com.info.baymax.common.queryapi.query.aggregate.AggQuery;
import com.info.baymax.common.queryapi.query.record.RecordQuery;
import com.info.baymax.common.queryapi.result.MapEntity;
import com.info.baymax.common.utils.ICollections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("dataReader")
@Slf4j
public class DefaultDataReader implements DataReader<MapEntity, MapEntity>, ApplicationContextAware {
    private final List<MapEntityDataReader> readers = Lists.newArrayList();

    @Override
    public void setApplicationContext(ApplicationContext context) {
        Map<String, MapEntityDataReader> beans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context,
            MapEntityDataReader.class, true, false);
        this.readers.addAll(beans.values());
        AnnotationAwareOrderComparator.sort(this.readers);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return true;
    }

    @Override
    public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
        try {
            return findSuitableReader(conf).readRecord(conf, query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataReadException(e.getMessage(), e);
        }
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        try {
            return findSuitableReader(conf).readAgg(conf, query);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DataReadException(e.getMessage(), e);
        }
    }

    private MapEntityDataReader findSuitableReader(StorageConf conf) {
        if (conf != null && ICollections.hasElements(readers)) {
            for (MapEntityDataReader reader : readers) {
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
