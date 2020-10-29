package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataCategory;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000,properties = {@Property(name = "cacheType",value = "${cacheType}")})
public interface DataCategoryMapper extends MyIdableMapper<DataCategory> {

    @Select("select max(ord)+1 from dsp_data_category where parent_id = #{parentId,jdbcType=VARCHAR}")
    int selectMaxOrder(@Param("parentId") Long parentId);

}
