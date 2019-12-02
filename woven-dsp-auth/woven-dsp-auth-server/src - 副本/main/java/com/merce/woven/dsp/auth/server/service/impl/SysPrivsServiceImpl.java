//package com.jusfoun.services.auth.server.service.impl;
//
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.stereotype.Service;
//
//import com.google.common.collect.Sets;
//import com.jusfoun.common.base.cache.CacheConsts;
//import com.jusfoun.common.base.tree.TreeableMapper;
//import com.jusfoun.common.enums.types.UsingStatus;
//import com.jusfoun.common.message.exception.ServiceException;
//import com.jusfoun.common.mybatis.mapper.MyBaseMapper;
//import com.jusfoun.common.mybatis.mapper.MyIdableMapper;
//import com.jusfoun.common.mybatis.mapper.extension.BaseExtensionSelectMapper;
//import com.jusfoun.common.utils.EntityUtils;
//import com.jusfoun.common.utils.ICollections;
//import com.jusfoun.services.auth.api.entity.SysPrivs;
//import com.jusfoun.services.auth.server.mapper.SysPrivsMapper;
//import com.jusfoun.services.auth.server.service.SysPrivsService;
//
///**
// * 说明：系统权限业务接口实现. <br>
// *
// * @author yjw@jusfoun.com
// * @date 2017年10月12日 下午5:50:49
// */
//@Service
//public class SysPrivsServiceImpl implements SysPrivsService {
//
//	@Autowired
//	private SysPrivsMapper sysPrivsMapper;
//
//	@Override
//	public BaseExtensionSelectMapper<SysPrivs> getBaseExtensionSelectMapper() {
//		return sysPrivsMapper;
//	}
//
//	@Override
//	public MyIdableMapper<SysPrivs> getMyIdableMapper() {
//		return sysPrivsMapper;
//	}
//
//	@Override
//	public MyBaseMapper<SysPrivs> getMyBaseMapper() {
//		return sysPrivsMapper;
//	}
//
//	@Override
//	public TreeableMapper<SysPrivs, Long> getTreeableMapper() {
//		return sysPrivsMapper;
//	}
//
//	// 清理所有的关于安全的缓存
//	@CacheEvict(value = CacheConsts.CACHE_SECURITY, allEntries = true)
//	@Override
//	public void initSysPrivss(SysPrivs root) throws ServiceException {
//		if (root == null)
//			return;
//		// 初始化一些属性
//		root.setCreatorId(root.getCreatorId() == null ? 1 : root.getCreatorId());
//		root.setUpdaterId(root.getUpdaterId() == null ? 1 : root.getUpdaterId());
//		Date date = new Date();
//		root.setCreateDate(root.getCreateDate() == null ? date : root.getCreateDate());
//		root.setUpdateDate(root.getUpdateDate() == null ? date : root.getUpdateDate());
//
//		root.setUpdaterName(StringUtils.isEmpty(root.getUpdaterName()) ? "admin" : root.getUpdaterName());
//		root.setCreatorName(StringUtils.isEmpty(root.getCreatorName()) ? "admin" : root.getCreatorName());
//
//		root.setRemark(StringUtils.isEmpty(root.getRemark()) ? root.getName() : root.getRemark());
//		root.setStatus(root.getStatus() == null ? UsingStatus.ENABLE.getValue() : root.getStatus());
//		saveSysPrivssByLoop(root);
//	}
//
//	/**
//	 * 说明： 以递归的方式保存权限节点. <br>
//	 *
//	 * @author yjw@jusfoun.com
//	 * @date 2017年10月12日 上午9:50:47
//	 * @param root
//	 *            根节点
//	 */
//	private void saveSysPrivssByLoop(SysPrivs root) {
//		if (root == null)
//			return;
//		sysPrivsMapper.insert(root);
//		// 保存子节点
//		List<SysPrivs> list = root.getSubList();
//		if (ICollections.hasElements(list)) {
//			Long pid = root.getId();
//			for (SysPrivs item : list) {
//				item.setParentId(pid);
//				item.setCreatorId(item.getCreatorId() == null ? root.getCreatorId() : item.getCreatorId());
//				item.setUpdaterId(item.getUpdaterId() == null ? root.getUpdaterId() : item.getUpdaterId());
//				item.setCreateDate(item.getCreateDate() == null ? root.getCreateDate() : item.getCreateDate());
//				item.setUpdateDate(item.getUpdateDate() == null ? root.getUpdateDate() : item.getUpdateDate());
//				item.setUpdaterName(StringUtils.isEmpty(item.getUpdaterName()) ? root.getUpdaterName() : item.getUpdaterName());
//				item.setCreatorName(StringUtils.isEmpty(item.getCreatorName()) ? root.getCreatorName() : item.getCreatorName());
//				item.setRemark(StringUtils.isEmpty(item.getRemark()) ? root.getName() : item.getRemark());
//				item.setStatus(item.getStatus() == null ? root.getStatus() : item.getStatus());
//				// 递归调用
//				saveSysPrivssByLoop(item);
//			}
//		}
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysprivs_' + #parentId", unless = "#result == null")
//	@Override
//	public List<SysPrivs> selectByParentId(Long parentId) throws ServiceException {
//		if (parentId == null)
//			parentId = 0L;
//		return sysPrivsMapper.selectByParentId(parentId);
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysprivs_url_'+ #clientId", unless = "#result == null")
//	@Override
//	public Set<String> selectUrlByClientId(String clientId) throws ServiceException {
//		List<SysPrivs> list = null;
//		if (StringUtils.isNotEmpty(clientId)) {
//			SysPrivs t = new SysPrivs();
//			t.setClientId(clientId);
//			list = select(t);
//		} else {
//			list = selectAll();
//		}
//		if (ICollections.hasElements(list)) {
//			return list.parallelStream().map(t -> t.getUrl()).collect(Collectors.toSet());
//		}
//		return Sets.newHashSet();
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysprivs_allauthorities'+#clientId", unless = "#result == null")
//	@Override
//	public Collection<String> selectAuthorities(String clientId) throws ServiceException {
//		return selectUrlByClientId(clientId);
//	}
//
//	@Cacheable(value = CacheConsts.CACHE_SECURITY, key = "'security_cache_sysprivs_byrootid'+#rootId", unless = "#result == null")
//	@Override
//	public SysPrivs selectTree(Long rootId) throws ServiceException {
//		return sysPrivsMapper.selectTree(EntityUtils.getDefaultIfNull(rootId, 1L));
//	}
//
//}
