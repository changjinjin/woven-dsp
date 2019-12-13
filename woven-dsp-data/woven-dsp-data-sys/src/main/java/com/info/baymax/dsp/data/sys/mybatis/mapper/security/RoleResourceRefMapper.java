package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;
import com.info.baymax.dsp.data.sys.entity.security.RoleResourceRef;

@Mapper
public interface RoleResourceRefMapper extends MyBaseMapper<RoleResourceRef> {

    @Delete("delete from ref_role_resource where role_id = #{roleId,jdbcType=BIGINT}")
    int deleteByRoleId(@Param("roleId") Long roleId);
}
