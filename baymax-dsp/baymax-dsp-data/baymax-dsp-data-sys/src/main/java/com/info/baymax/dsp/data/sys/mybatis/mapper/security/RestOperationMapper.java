package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.mybatis.type.varchar.VarcharVsStringListTypeHandler;
import com.info.baymax.dsp.data.sys.entity.security.RestOperation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.BooleanTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.util.Set;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 300, properties = {
    @Property(name = "cacheType", value = "${cacheType}")})
public interface RestOperationMapper extends MyIdableMapper<RestOperation> {
    /**
     * 根据权限ID查询接口列表
     *
     * @param permId 权限ID
     * @return 权限对应的接口列表
     */
    // @formatter:off
    @Select({"select o.* from merce_rest_operation o inner join ref_perm_operation rr on o.id = rr.operation_id where rr.perm_id = #{permId,jdbcType=VARCHAR}"})
    @Results(
        id = "BaseResultMap",
        value = {
            @Result(column = "id", jdbcType = JdbcType.VARCHAR, property = "id", id = true),
            @Result(column = "service_name", jdbcType = JdbcType.VARCHAR, property = "serviceName"),
            @Result(column = "group_name", jdbcType = JdbcType.VARCHAR, property = "groupName"),
            @Result(column = "tags", jdbcType = JdbcType.VARCHAR, property = "tags", typeHandler = VarcharVsStringListTypeHandler.class),
            @Result(column = "method", jdbcType = JdbcType.VARCHAR, property = "method"),
            @Result(column = "base_path", jdbcType = JdbcType.VARCHAR, property = "basePath"),
            @Result(column = "relative_path", jdbcType = JdbcType.VARCHAR, property = "relativePath"),
            @Result(column = "full_path", jdbcType = JdbcType.VARCHAR, property = "fullPath"),
            @Result(column = "summary", jdbcType = JdbcType.VARCHAR, property = "summary"),
            @Result(column = "description", jdbcType = JdbcType.VARCHAR, property = "description"),
            @Result(column = "operationId", jdbcType = JdbcType.VARCHAR, property = "operationId"),
            @Result(column = "consumes", jdbcType = JdbcType.VARCHAR, property = "consumes", typeHandler = VarcharVsStringListTypeHandler.class),
            @Result(column = "produces", jdbcType = JdbcType.VARCHAR, property = "produces", typeHandler = VarcharVsStringListTypeHandler.class),
            @Result(column = "deprecated", jdbcType = JdbcType.BIT, property = "deprecated", typeHandler = BooleanTypeHandler.class),
            @Result(column = "enabled", jdbcType = JdbcType.BIT, property = "enabled", typeHandler = BooleanTypeHandler.class),
        })
    // @formatter:on
    Set<RestOperation> selectByPermId(@Param("permId") String permId);
}
