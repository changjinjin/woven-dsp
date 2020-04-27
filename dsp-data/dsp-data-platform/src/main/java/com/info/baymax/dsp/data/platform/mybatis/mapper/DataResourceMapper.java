package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface DataResourceMapper extends MyIdableMapper<DataResource> {

    @Update({
            "<script>",
            "update",
            "dsp_data_resource",
            "set open_status = 0",
            "where id in",
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    void closeDataResourceByIds(@Param("ids") List<Long> ids);
}
