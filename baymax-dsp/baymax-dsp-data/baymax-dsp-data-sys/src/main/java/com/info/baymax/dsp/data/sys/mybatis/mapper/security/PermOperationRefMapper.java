package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.PermOperationRef;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Property;

@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 500, properties = {
    @Property(name = "cacheType", value = "${cacheType}")})
public interface PermOperationRefMapper extends MyBaseMapper<PermOperationRef> {

    @Delete("delete from ref_perm_operation where perm_id = #{permId,jdbcType=VARCHAR}")
    int deleteByPermId(@Param("permId") String permId);
}
