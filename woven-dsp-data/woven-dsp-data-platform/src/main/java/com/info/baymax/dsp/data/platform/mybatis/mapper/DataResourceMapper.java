package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.info.baymax.dsp.data.platform.entity.DataResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DataResourceMapper extends MyBaseMapper<DataResource> , DeleteByPrimaryKeysMapper {

	@Delete("delete from ref_role_permission where role_id = #{roleId,jdbcType=BIGINT}")
	int deleteByRoleId(@Param("roleId") Long roleId);

	@Delete("delete from ref_role_permission where permission_id = #{permissionId,jdbcType=BIGINT}")
	int deleteByPermissionId(@Param("permissionId") Long permissionId);

}
