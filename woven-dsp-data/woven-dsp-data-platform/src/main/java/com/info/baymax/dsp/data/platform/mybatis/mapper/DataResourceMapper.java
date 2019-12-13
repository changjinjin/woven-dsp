package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.RolePermissionRef;

@Mapper
public interface RolePermissionRefMapper extends MyBaseMapper<RolePermissionRef> {

	@Delete("delete from ref_role_permission where role_id = #{roleId,jdbcType=BIGINT}")
	int deleteByRoleId(@Param("roleId") Long roleId);

	@Delete("delete from ref_role_permission where permission_id = #{permissionId,jdbcType=BIGINT}")
	int deleteByPermissionId(@Param("permissionId") Long permissionId);

}
