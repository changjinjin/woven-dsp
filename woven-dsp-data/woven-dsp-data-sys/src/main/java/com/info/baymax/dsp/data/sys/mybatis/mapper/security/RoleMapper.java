package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Role;

@Mapper
public interface RoleMapper extends MyIdableMapper<Role> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 用户对应的角色列表
     */
    List<Role> selectOnlyRoleByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 用户对应的角色列表
     */
    List<Role> selectWithPermsByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID查询
     *
     * @param id 角色ID
     * @return 角色信息，包含对应的权限信息
     */
    Role selectWithPermissionsById(@Param("id") String id);
}
