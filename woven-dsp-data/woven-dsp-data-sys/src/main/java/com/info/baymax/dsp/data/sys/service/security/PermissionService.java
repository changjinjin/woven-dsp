package com.info.baymax.dsp.data.sys.service.security;

import java.util.List;

import com.info.baymax.common.entity.base.BaseEntityService;
import com.info.baymax.common.service.tree.id.TreeIdableService;
import com.info.baymax.dsp.data.sys.entity.security.Permission;

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
	 * @param clientId 客户端ID
	 * @param code     编码
	 * @return 存在的节点，不存在返回空
	 */
	Permission findByClientIdAndCode(String clientId, String code);

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
	 * @param clientId 客户端ID
	 * @return 最大的order值
	 */
	int selectMaxOrder(String clientId);

}
