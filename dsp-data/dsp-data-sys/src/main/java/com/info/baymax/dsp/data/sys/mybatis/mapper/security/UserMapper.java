package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.mybatis.type.base64.varchar.GZBase64VarcharVsListStringTypeHandler;
import com.info.baymax.dsp.data.sys.entity.security.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.Map;

@Mapper
public interface UserMapper extends MyIdableMapper<User> {

    /**
     * 根据ID查询用户信息一并查出角色信息
     *
     * @param params 查询参数
     * @return 用户信息包含级联的角色信息和权限信息
     */
    // @formatter:off
    @Select({"<script> "
        + "select * from merce_user t"
        + "<where>"
        + "	<if test=\"id != null and id != ''\"> and id = #{id,jdbcType=VARCHAR}</if>"
        + "	<if test=\"tenantId != null and tenantId != ''\">and tenant_id = #{tenantId,jdbcType=VARCHAR}</if>"
        + "	<if test=\"loginId != null and loginId != ''\">and login_id = #{loginId,jdbcType=VARCHAR}</if>"
        + "	<if test=\"owner != null and owner != ''\">and owner = #{owner,jdbcType=VARCHAR}</if>"
        + "	<if test=\"version != null and version != ''\">and version = #{version,jdbcType=VARCHAR}</if>"
        + "	<if test=\"enabled != null\">and enabled = #{enabled,jdbcType=INTEGER}</if>"
        + "	<if test=\"clientIds != null and clientIds != ''\">"
        + "		<bind name=\"pattern\" value=\"'%'+clientIds+'%'\" />"
        + "		and client_ids like #{clientIds,jdbcType=VARCHAR}"
        + "	</if>"
        + "</where>"
        + "</script>"})
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
            @Result(column = "hdfs_space_quota", jdbcType = JdbcType.BIGINT, property = "hdfsSpaceQuota"),
            @Result(column = "login_id", jdbcType = JdbcType.VARCHAR, property = "loginId"),
            @Result(column = "password", jdbcType = JdbcType.VARCHAR, property = "password"),
            @Result(column = "resource_queues", jdbcType = JdbcType.VARCHAR, property = "resourceQueues", typeHandler = GZBase64VarcharVsListStringTypeHandler.class),
            @Result(column = "tenant_id", jdbcType = JdbcType.VARCHAR, property = "tenantId"),
            @Result(column = "is_admin", jdbcType = JdbcType.INTEGER, property = "admin"),
            @Result(column = "email", jdbcType = JdbcType.VARCHAR, property = "email"),
            @Result(column = "phone", jdbcType = JdbcType.VARCHAR, property = "phone"),
            @Result(column = "account_expired_time", jdbcType = JdbcType.TIMESTAMP, property = "accountExpiredTime"),
            @Result(column = "pwd_expired_time", jdbcType = JdbcType.TIMESTAMP, property = "pwdExpiredTime"),
            @Result(column = "client_ids", jdbcType = JdbcType.VARCHAR, property = "clientIds"),
            @Result(column = "id", property = "roles", many = @Many(select = "com.info.baymax.dsp.data.sys.mybatis.mapper.security.RoleMapper.selectWithPermsByUserId", fetchType = FetchType.EAGER))
        })
    // @formatter:on
    User selectOneWithRoles(Map<String, Object> params);
}
