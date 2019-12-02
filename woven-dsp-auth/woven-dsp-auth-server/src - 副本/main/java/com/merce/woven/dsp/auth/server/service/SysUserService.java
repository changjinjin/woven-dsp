//package com.jusfoun.services.auth.server.service;
//
//import com.jusfoun.common.base.extend.entity.BaseEntityWithAssociateService;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.services.auth.api.entity.SysUser;
//
///**
// * 说明： 用户管理. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年10月12日 下午5:51:27
// */
//public interface SysUserService extends BaseEntityWithAssociateService<SysUser> {
//	/**
//	 * 说明： 根据用户名名称查询用户. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年9月20日 上午9:45:07
//	 * @param username
//	 *            用户名称
//	 * @return 用户信息
//	 * @throws ServiceException
//	 */
//	SysUser selectByUsername(String username) throws ServiceException;
//
//	/**
//	 * 说明： 修改用户角色并且缓存用户信息. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年10月12日 下午4:38:44
//	 * @param sysUser
//	 *            用户信息
//	 * @throws ServiceException
//	 */
//	void modifyRoles(SysUser sysUser) throws ServiceException;
//
//	/**
//	 * 说明： 删除用户信息和用户角色信息，并且缓存用户信息. <br>
//	 * 
//	 * @author yjw@jusfoun.com
//	 * @date 2017年11月2日 上午11:09:03
//	 * @param id
//	 *            用户ID
//	 * @throws ServiceException
//	 */
//	int deleteWithRoles(Long id) throws ServiceException;
//
//	/**
//	 * 说明：保存用户信息并保存. <br>
//	 * 
//	 * @author yjw@jusfoun.com
//	 * @date 2017年11月27日 下午2:23:42
//	 * @param sysUser
//	 * @return
//	 */
//	int insertWithCache(SysUser sysUser);
//
//	/**
//	 * 说明： 根据主键选择新的更新用户信息并且缓存数据. <br>
//	 * 
//	 * @author yjw@jusfoun.com
//	 * @date 2017年11月23日 下午7:50:42
//	 * @param sysUser
//	 *            用户信息
//	 * @return
//	 */
//	void updateByPrimaryKeySelectiveWithCache(SysUser sysUser);
//
//	/**
//	 * 说明： 根据主键查询用户信息并且缓存. <br>
//	 * 
//	 * @author yjw@jusfoun.com
//	 * @date 2017年11月23日 下午7:49:24
//	 * @param id
//	 *            主键
//	 * @return 用户信息
//	 * @throws ServiceException
//	 */
//	SysUser selectPKWithCache(Long id) throws ServiceException;
//
//}
