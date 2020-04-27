package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.RolePermissionRef;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 500, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface RolePermissionRefMapper extends MyBaseMapper<RolePermissionRef> {

	@Delete("delete from ref_role_permission where role_id = #{roleId,jdbcType=VARCHAR}")
	int deleteByRoleId(@Param("roleId") String roleId);

	@Delete("delete from ref_role_permission where permission_id = #{permissionId,jdbcType=VARCHAR}")
	int deleteByPermissionId(@Param("permissionId") String permissionId);

}
