package com.merce.woven.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.merce.woven.common.mybatis.mapper.MyBaseMapper;
import com.merce.woven.dsp.data.sys.entity.security.UserRoleRef;

@Mapper
public interface UserRoleRefMapper extends MyBaseMapper<UserRoleRef> {

	@Delete("delete from ref_user_role where user_id = #{userId,jdbcType=BIGINT}")
	int deleteByUserId(@Param("userId") Long userId);

	@Delete("delete from ref_user_role where role_id = #{roleId,jdbcType=BIGINT}")
	int deleteByRoleId(@Param("roleId") Long roleId);
}
