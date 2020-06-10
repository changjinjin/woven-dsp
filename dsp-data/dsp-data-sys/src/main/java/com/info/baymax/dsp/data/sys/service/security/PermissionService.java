package com.info.baymax.dsp.data.sys.service.security;

import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.tree.id.TreeIdableService;
import com.info.baymax.dsp.data.sys.entity.security.Permission;

import java.util.List;

public interface PermissionService
		extends BaseIdableAndExampleQueryService<String, Permission>, TreeIdableService<String, Permission> {

	/**
	 * 查询所有的权限值集合
	 * @return 所有的权限值集合
	 */
	List<String> findAllAuthorities();

	/**
	 * 查询权限树，返回根节点列表
	 *
	 * @return 根节点列表
	 */
	List<Permission> findRootsTree();

	/**
	 * 查询节点
	 *
	 * @param code 编码
	 * @return 存在的节点，不存在返回空
	 */
	Permission findByCode(String code);

	/**
	 * 根据ID级联删除
	 *
	 * @param id 要删除的顶级ID
	 * @return 删除结果
	 */
	int deleteOnCascadeById(String id);

	/**
	 * 查询权限最大的order排序号
	 *
	 * @return 最大的order值
	 */
	int selectMaxOrder();

}
