package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

@Mapper
public interface PermissionMapper extends MyIdableMapper<Permission> {

	/**
	 * 根据角色ID查询权限列表
	 *
	 * @param roleId 角色ID
	 * @return 角色对应的权限列表
	 */
	// @formatter:off
	@Select({"select p.*,rr.half_select from dsp_sys_menu p left join ref_role_permission rr on p.id = rr.permission_id where rr.role_id = #{roleId,jdbcType=VARCHAR}"})
	@Results(
	    id = "BaseResultMap",
		value = {
			@Result(column="id",jdbcType=JdbcType.VARCHAR,property="id",id=true),
			@Result(column="code",jdbcType=JdbcType.VARCHAR,property="code"),
			@Result(column="name",jdbcType=JdbcType.VARCHAR,property="name"),
			@Result(column="create_time",jdbcType=JdbcType.TIMESTAMP,property="createTime"),
			@Result(column="creator",jdbcType=JdbcType.VARCHAR,property="creator"),
			@Result(column="enabled",jdbcType=JdbcType.INTEGER,property="enabled"),
			@Result(column="expired_time",jdbcType=JdbcType.BIGINT,property="expiredTime"),
			@Result(column="last_modified_time",jdbcType=JdbcType.TIMESTAMP,property="lastModifiedTime"),
			@Result(column="last_modifier",jdbcType=JdbcType.VARCHAR,property="lastModifier"),
			@Result(column="owner",jdbcType=JdbcType.VARCHAR,property="owner"),
			@Result(column="version",jdbcType=JdbcType.INTEGER,property="version"),
			@Result(column="type",jdbcType=JdbcType.INTEGER,property="type"),
			@Result(column="url",jdbcType=JdbcType.VARCHAR,property="url"),
			@Result(column="tenant_id",jdbcType=JdbcType.VARCHAR,property="tenantId"),
			@Result(column="parent_id",jdbcType=JdbcType.VARCHAR,property="parentId"),
			@Result(column="half_select",jdbcType=JdbcType.INTEGER,property="halfSelect"),
			@Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
			@Result(column="route",jdbcType=JdbcType.VARCHAR,property="route"),
			@Result(column="ord",jdbcType=JdbcType.VARCHAR,property="order"),
			@Result(column="client_id",jdbcType=JdbcType.VARCHAR,property="clientId"),
	    })
	// @formatter:on
	Set<Permission> selectByRoleId(@Param("roleId") String roleId);

	/**
	 * 根据父节点ID查询权限列表
	 *
	 * @param parentId 父节点ID
	 * @return 子节点集合
	 */
	// @formatter:off
	@Select({"select * from dsp_sys_menu t where t.parent_id = #{parentId,jdbcType=VARCHAR}"})
	@Results(
		id = "BaseWithChildrenResultMap",
		value = {
			@Result(column="id",jdbcType=JdbcType.VARCHAR,property="id",id=true),
			@Result(column="code",jdbcType=JdbcType.VARCHAR,property="code"),
			@Result(column="name",jdbcType=JdbcType.VARCHAR,property="name"),
			@Result(column="create_time",jdbcType=JdbcType.TIMESTAMP,property="createTime"),
			@Result(column="creator",jdbcType=JdbcType.VARCHAR,property="creator"),
			@Result(column="enabled",jdbcType=JdbcType.INTEGER,property="enabled"),
			@Result(column="expired_time",jdbcType=JdbcType.BIGINT,property="expiredTime"),
			@Result(column="last_modified_time",jdbcType=JdbcType.TIMESTAMP,property="lastModifiedTime"),
			@Result(column="last_modifier",jdbcType=JdbcType.VARCHAR,property="lastModifier"),
			@Result(column="owner",jdbcType=JdbcType.VARCHAR,property="owner"),
			@Result(column="version",jdbcType=JdbcType.INTEGER,property="version"),
			@Result(column="type",jdbcType=JdbcType.INTEGER,property="type"),
			@Result(column="url",jdbcType=JdbcType.VARCHAR,property="url"),
			@Result(column="tenant_id",jdbcType=JdbcType.VARCHAR,property="tenantId"),
			@Result(column="parent_id",jdbcType=JdbcType.VARCHAR,property="parentId"),
			@Result(column="half_select",jdbcType=JdbcType.INTEGER,property="halfSelect"),
			@Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
			@Result(column="route",jdbcType=JdbcType.VARCHAR,property="route"),
			@Result(column="ord",jdbcType=JdbcType.VARCHAR,property="order"),
			@Result(column="client_id",jdbcType=JdbcType.VARCHAR,property="clientId"),
			@Result(column="id",property="children",many=@Many(select="selectByParentId",fetchType= FetchType.EAGER))
		})
	// @formatter:on
	List<Permission> selectByParentId(@Param("parentId") String parentId);

	/**
	 * 级联查询一个权限记录和他的所有子孙节点
	 *
	 * @param id 主键值
	 * @return 权限记录和他的所有子孙节点
	 */
	// @formatter:off
	@Select({"select * from dsp_sys_menu t where t.id = #{id,jdbcType=VARCHAR}"})
	@ResultMap(value = {"BaseWithChildrenResultMap"})
	// @formatter:on
	Permission selectOneWithChildren(@Param("id") String id);

	@Select("select max(ord) from dsp_sys_menu where client_id = #{clientId,jdbcType=VARCHAR}")
	int selectMaxOrder(String clientId);
}
