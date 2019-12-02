//package com.jusfoun.services.auth.server.mapper;
//
//import org.apache.ibatis.annotations.Mapper;
//
//import com.jusfoun.common.base.tree.TreeableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseIdableExtensionMapper;
//import com.jusfoun.services.auth.api.entity.SysGov;
//
//@Mapper
//public interface SysGovMapper extends BaseIdableExtensionMapper<SysGov>, TreeableMapper<SysGov, Long> {
//	/**
//	 * 说明：根据ID 查询名称. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年9月23日 上午10:53:35
//	 * @param id
//	 *            ID
//	 * @return 名称
//	 */
//	String selectNameByPrimaryKey(Long id);
//
//}