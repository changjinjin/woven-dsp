package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.UserRoleRef;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 500, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface UserRoleRefMapper extends MyBaseMapper<UserRoleRef> {

	@Delete("delete from ref_user_role where user_id = #{userId,jdbcType=VARCHAR}")
	int deleteByUserId(@Param("userId") String userId);

	@Delete("delete from ref_user_role where role_id = #{roleId,jdbcType=VARCHAR}")
	int deleteByRoleId(@Param("roleId") String roleId);
}
