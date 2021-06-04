package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.DataSource;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 500, properties = {@Property(name = "cacheType", value = "${cacheType}")})
public interface DataSourceMapper extends MyIdableMapper<DataSource> {

    @Options(useCache = false)
    @Select("select distinct t.type from merce_dss t where t.tenant = #{tenantId,jdbcType=VARCHAR}")
    List<String> getDistinctTypes(@Param("tenantId") String tenantId);

    @Options(useCache = false)
    @Select("select * from merce_dss t where t.name = #{name,jdbcType=VARCHAR}")
    DataSource selectEntityByName(@Param("name") String name);
}
