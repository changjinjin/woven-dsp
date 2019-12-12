package com.merce.woven.dsp.data.sys.service.security;

import java.util.List;

import com.merce.woven.common.entity.base.BaseEntityService;
import com.merce.woven.common.service.tree.id.TreeIdableService;
import com.merce.woven.dsp.data.sys.entity.security.Permission;

public interface PermissionService extends BaseEntityService<Permission>, TreeIdableService<Long, Permission> {

	/**
	 * 查询所有的权限值集合
	 *
	 * @param clientId 客户端ID
	 * @return 所有的权限值集合
	 */
	List<String> findAuthoritiesByClientId(String clientId);

	/**
	 * 查询权限树，返回根节点列表
	 *
	 * @return 根节点列表
	 */
	List<Permission> findRootsTree();

	/**
	 * 查询节点
	 *
	 * @param tenantId 当前租户ID
	 * @param code     编码
	 * @return 存在的节点，不存在返回空
	 */
	Permission existsByTenantIdAndCode(Long tenantId, String code);

	/**
	 * 根据ID级联删除
	 *
	 * @param id 要删除的顶级ID
	 * @return 删除结果
	 */
	int deleteOnCascadeById(Long id);

	/**
	 * 查询权限最大的order排序号
	 *
	 * @param tenantId 租户ID
	 * @return 最大的order值
	 */
	int selectMaxOrder(Long tenantId);

}
