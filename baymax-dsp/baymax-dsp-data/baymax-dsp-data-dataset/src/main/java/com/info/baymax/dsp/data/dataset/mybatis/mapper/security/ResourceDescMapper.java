package com.info.baymax.dsp.data.dataset.mybatis.mapper.security;

import com.info.baymax.common.persistence.mybatis.cache.RoutingCache;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import org.apache.ibatis.annotations.*;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 1000, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
public interface ResourceDescMapper extends MyIdableMapper<ResourceDesc> {
	@Update("UPDATE merce_resource_dir t SET t.path = CONCAT(#{newPath},SUBSTR(t.path, #{length}, LENGTH(t.path))) WHERE t.path like #{oldPath}")
	int updatePath(@Param("oldPath") String oldPath, @Param("length") int length, @Param("newPath") String newPath);

	@Select("select max(t.ord) from merce_resource_dir t where t.tenant_id = #{tenantId} and t.parent_id = #{parentId}")
	Integer findMaxOrderByParent(@Param("tenantId") String tenantId, @Param("parentId") String parentId);

	@Delete("delete from merce_resource_dir where owner not in (select id from merce_user)")
	int deleteResDirWhereOwnerNotFound();
}
