//package com.jusfoun.services.auth.server.mapper;
//
//import java.util.List;
//
//import org.apache.ibatis.annotations.Mapper;
//
//import com.jusfoun.common.base.tree.TreeableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseIdableExtensionMapper;
//import com.jusfoun.services.auth.api.entity.SysPrivs;
//
//@Mapper
//public interface SysPrivsMapper extends BaseIdableExtensionMapper<SysPrivs>, TreeableMapper<SysPrivs, Long> {
//
//	/**
//	 * 说明：根据角色ID查询角色拥有的权限列表. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年9月20日 上午9:16:03
//	 * @param roleId
//	 *            角色ID
//	 * @return 权限列表
//	 */
//	List<SysPrivs> selectByRoleId(Long roleId);
//
//}