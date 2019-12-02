//package com.jusfoun.services.auth.server.service;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Set;
//
//import com.jusfoun.common.base.extend.entity.BaseEntityWithAssociateService;
//import com.jusfoun.common.base.tree.TreeableAndIdableService;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.services.auth.api.entity.SysPrivs;
//
///**
// * 说明： 系统权限管理. <br>
// * 
// * @author yjw@jusfoun.com
// * @date 2017年10月12日 下午5:50:25
// */
//public interface SysPrivsService extends BaseEntityWithAssociateService<SysPrivs>, TreeableAndIdableService<SysPrivs, Long> {
//
//	/**
//	 * 说明： 初始化系统权限. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年10月12日 上午9:46:11
//	 * @param root
//	 *            根权限对象，包含子权限集合则要保存子权限
//	 * @throws ServiceException
//	 */
//	void initSysPrivss(SysPrivs root) throws ServiceException;
//
//	/**
//	 * 说明：根据父节点ID查询权限列表. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年9月20日 上午9:16:03
//	 * @param roleId
//	 *            角色ID
//	 * @return 权限列表
//	 * @throws ServiceException
//	 */
//	List<SysPrivs> selectByParentId(Long parentId) throws ServiceException;
//
//	/**
//	 * 说明： 查询系统已有的所有权限值. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年10月12日 上午9:46:11
//	 * @throws ServiceException
//	 */
//	Set<String> selectUrlByClientId(String clientId) throws ServiceException;
//
//	/**
//	 * 说明： 查询权限对象. <br>
//	 * 
//	 * @author yjw@jusfoun.com
//	 * @date 2017年12月5日 下午5:07:15
//	 * @param clientId
//	 * @return
//	 * @throws ServiceException
//	 */
//	Collection<String> selectAuthorities(String clientId) throws ServiceException;
//
//}
