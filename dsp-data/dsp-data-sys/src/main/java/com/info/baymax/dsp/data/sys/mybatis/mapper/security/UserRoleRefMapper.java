package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.UserRoleRef;

@Mapper
public interface UserRoleRefMapper extends MyBaseMapper<UserRoleRef> {

	@Delete("delete from ref_user_role where user_id = #{userId,jdbcType=VARCHAR}")
	int deleteByUserId(@Param("userId") String userId);

	@Delete("delete from ref_user_role where role_id = #{roleId,jdbcType=VARCHAR}")
	int deleteByRoleId(@Param("roleId") String roleId);
}
