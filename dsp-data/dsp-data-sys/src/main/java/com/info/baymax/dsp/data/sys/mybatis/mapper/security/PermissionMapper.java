package com.info.baymax.dsp.data.sys.mybatis.mapper.security;

import com.info.baymax.common.mybatis.cache.RoutingCache;
import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.sys.entity.security.Permission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;
import java.util.Set;

@Mapper
@CacheNamespace(implementation = RoutingCache.class, readWrite = false, flushInterval = 600000, size = 200, properties = {
		@Property(name = "cacheType", value = "${cacheType}") })
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
			@Result(column="enabled",jdbcType=JdbcType.INTEGER,property="enabled"),
			@Result(column="type",jdbcType=JdbcType.INTEGER,property="type"),
			@Result(column="url",jdbcType=JdbcType.VARCHAR,property="url"),
			@Result(column="parent_id",jdbcType=JdbcType.VARCHAR,property="parentId"),
			@Result(column="half_select",jdbcType=JdbcType.INTEGER,property="halfSelect"),
			@Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
			@Result(column="route",jdbcType=JdbcType.VARCHAR,property="route"),
			@Result(column="ord",jdbcType=JdbcType.VARCHAR,property="order"),
			@Result(column="client_id",jdbcType=JdbcType.VARCHAR,property="clientId"),
			@Result(column="id",property="operations",many=@Many(select="com.info.baymax.dsp.data.sys.mybatis.mapper.security.RestOperationMapper.selectByPermId",fetchType= FetchType.EAGER)),
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
			@Result(column="enabled",jdbcType=JdbcType.INTEGER,property="enabled"),
			@Result(column="type",jdbcType=JdbcType.INTEGER,property="type"),
			@Result(column="url",jdbcType=JdbcType.VARCHAR,property="url"),
			@Result(column="parent_id",jdbcType=JdbcType.VARCHAR,property="parentId"),
			@Result(column="half_select",jdbcType=JdbcType.INTEGER,property="halfSelect"),
			@Result(column="icon",jdbcType=JdbcType.VARCHAR,property="icon"),
			@Result(column="route",jdbcType=JdbcType.VARCHAR,property="route"),
			@Result(column="ord",jdbcType=JdbcType.VARCHAR,property="order"),
			@Result(column="client_id",jdbcType=JdbcType.VARCHAR,property="clientId"),
			@Result(column="id",property="operations",many=@Many(select="com.info.baymax.dsp.data.sys.mybatis.mapper.security.RestOperationMapper.selectByPermId",fetchType= FetchType.EAGER)),
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

	@Select("select max(ord) from dsp_sys_menu")
	int selectMaxOrder();
}
