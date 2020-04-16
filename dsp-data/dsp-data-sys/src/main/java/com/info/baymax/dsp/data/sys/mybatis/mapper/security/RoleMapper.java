package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface RoleMapper extends MyIdableMapper<Role> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 用户对应的角色列表
     */
    // @formatter:off
    @Select({"select * from merce_role r inner join ref_user_role ur on r.id = ur.role_id where ur.user_id = #{userId,jdbcType=VARCHAR}"})
    @Results(
        id = "BaseResultMap",
        value = {
            @Result(column = "id", jdbcType = JdbcType.VARCHAR, property = "id", id = true),
            @Result(column = "create_time", jdbcType = JdbcType.TIMESTAMP, property = "createTime"),
            @Result(column = "creator", jdbcType = JdbcType.VARCHAR, property = "creator"),
            @Result(column = "enabled", jdbcType = JdbcType.INTEGER, property = "enabled"),
            @Result(column = "expired_time", jdbcType = JdbcType.BIGINT, property = "expiredTime"),
            @Result(column = "last_modified_time", jdbcType = JdbcType.TIMESTAMP, property = "lastModifiedTime"),
            @Result(column = "last_modifier", jdbcType = JdbcType.VARCHAR, property = "lastModifier"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "owner", jdbcType = JdbcType.VARCHAR, property = "owner"),
            @Result(column = "version", jdbcType = JdbcType.INTEGER, property = "version"),
            @Result(column = "tenant_id", jdbcType = JdbcType.VARCHAR, property = "tenantId")
        })
    // @formatter:on
    List<Role> selectOnlyRoleByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 用户对应的角色列表
     */
    // @formatter:off
    @Select({"select * from merce_role r inner join ref_user_role ur on r.id = ur.role_id where ur.user_id = #{userId,jdbcType=VARCHAR}"})
    @Results(
        id = "BaseWithPermissionsResultMap",
        value = {
            @Result(column = "id", jdbcType = JdbcType.VARCHAR, property = "id", id = true),
            @Result(column = "create_time", jdbcType = JdbcType.TIMESTAMP, property = "createTime"),
            @Result(column = "creator", jdbcType = JdbcType.VARCHAR, property = "creator"),
            @Result(column = "enabled", jdbcType = JdbcType.INTEGER, property = "enabled"),
            @Result(column = "expired_time", jdbcType = JdbcType.BIGINT, property = "expiredTime"),
            @Result(column = "last_modified_time", jdbcType = JdbcType.TIMESTAMP, property = "lastModifiedTime"),
            @Result(column = "last_modifier", jdbcType = JdbcType.VARCHAR, property = "lastModifier"),
            @Result(column = "name", jdbcType = JdbcType.VARCHAR, property = "name"),
            @Result(column = "owner", jdbcType = JdbcType.VARCHAR, property = "owner"),
            @Result(column = "version", jdbcType = JdbcType.INTEGER, property = "version"),
            @Result(column = "tenant_id", jdbcType = JdbcType.VARCHAR, property = "tenantId"),
            @Result(column = "id", property = "permissions", many = @Many(select = "com.info.baymax.dsp.data.sys.mybatis.mapper.security.PermissionMapper.selectByRoleId", fetchType = FetchType.EAGER))
        })
    // @formatter:on
    List<Role> selectWithPermsByUserId(@Param("userId") String userId);

    /**
     * 根据角色ID查询
     *
     * @param id 角色ID
     * @return 角色信息，包含对应的权限信息
     */
    // @formatter:off
    @Select({"select * from merce_role r where r.id = #{id,jdbcType=VARCHAR}"})
    @ResultMap(value = {"BaseWithPermissionsResultMap"})
    // @formatter:on
    Role selectWithPermissionsById(@Param("id") String id);
}
