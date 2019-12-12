package com.merce.woven.dsp.data.sys.mybatis.mapper.security;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.dsp.data.sys.entity.security.Permission;

@Mapper
public interface PermissionMapper extends MyIdableMapper<Permission> {

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 角色对应的权限列表
     */
    Set<Permission> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据父节点ID查询权限列表
     *
     * @param parentId 父节点ID
     * @return 子节点集合
     */
    List<Permission> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 级联查询一个权限记录和他的所有子孙节点
     *
     * @param id 主键值
     * @return 权限记录和他的所有子孙节点
     */
    Permission selectOneWithChildren(@Param("id") Long id);

    @Select("select max(ord) from merce_permission where tenant_id = #{tenantId,jdbcType=BIGINT}")
    int selectMaxOrder(Long tenantId);
}
