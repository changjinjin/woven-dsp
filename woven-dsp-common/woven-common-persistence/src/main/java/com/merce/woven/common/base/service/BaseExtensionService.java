package com.merce.woven.common.base.service;

import com.github.pagehelper.Page;
import com.google.common.collect.Maps;
import com.merce.woven.common.message.exception.ServiceException;
import com.merce.woven.common.mybatis.mapper.extension.BaseExtensionSelectMapper;
import com.merce.woven.common.mybatis.page.IPage;
import com.merce.woven.common.mybatis.page.IPageable;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些通用的扩展查询接口，可用于涉及到关联数据的查询等
 *
 * @author jingwei.yang
 * @date 2019-05-29 14:54
 */
@Transactional
public interface BaseExtensionService<T> extends BaseService<T> {

	BaseExtensionSelectMapper<T> getBaseExtensionSelectMapper();

	/**
	 * 根据条件查询唯一一条记录. <br>
	 *
	 * @param params 查询条件
	 * @return 符合条件的唯一一条记录，没查到返回空，如果多条则会抛出异常
	 */
	default T selectExtensionOne(Map<?, ?> params) throws ServiceException {
		return getBaseExtensionSelectMapper().selectExtensionOne(params);
	}

	/**
	 * 根据主键查询唯一的记录（当主键名称默认的“id”）. <br>
	 *
	 * @param id 主键值
	 * @return 主键对应记录，没查到返回空
	 */
	default T selectExtensionByPrimaryKey(Object id) throws ServiceException {
		return selectExtensionByPrimaryKey("id", id);
	}

	/**
	 * 根据主键查询唯一的记录（当主键名称不是默认的“id”时，指定主键名称和主键值）. <br>
	 *
	 * @param primaryKeyName  主键名称
	 * @param primaryKeyValue 主键值
	 * @return 主键对应记录，无记录返回空
	 */
	default T selectExtensionByPrimaryKey(String primaryKeyName, Object primaryKeyValue) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		// 默认的主键名称为id，也可以修改改参数同时传入主键名称和主键值
		params.put(primaryKeyName, primaryKeyValue);
		return selectExtensionOne(params);
	}

	/**
	 * 查询条数，有模糊查询等参数. <br>
	 *
	 * @param params 查询条件
	 * @return 符合条件的数据总条数
	 * @throws ServiceException
	 */
	default int selectExtensionCount(Map<?, ?> params) throws ServiceException {
		return getBaseExtensionSelectMapper().selectExtensionCount(params);
	}

	/**
	 * 根据条件查询所有数据，附加关联字段. <br>
	 *
	 * @param params        查询条件
	 * @param orderByClause 排序条件，根据查询的字段排序，如：“id asc,name desc”
	 * @return 查询结果集
	 * @throws ServiceException
	 */
	default List<T> selectExtensionList(Map<?, ?> params, String orderByClause) throws ServiceException {
		startOrderBy(orderByClause);// 排序
		return selectExtensionList(params);
	}

	/**
	 * 根据条件查询所有数据，附加关联字段. <br>
	 *
	 * @param params 查询条件
	 * @return 查询结果集
	 * @throws ServiceException
	 */
	default List<T> selectExtensionList(Map<?, ?> params) throws ServiceException {
		if (params == null) {
			params = Maps.newHashMap();
		}
		return getBaseExtensionSelectMapper().selectExtensionList(params);
	}

	/**
	 * 查询分页列表包含附加属性. <br>
	 *
	 * @param params 查询条件
	 * @param pageable   分页信息，传空查询所有
	 * @return 扩展查询数据列表
	 * @throws ServiceException
	 */
	default List<T> selectExtensionList(Map<?, ?> params, IPageable pageable) throws ServiceException {
		startPage(pageable);
		return getBaseExtensionSelectMapper().selectExtensionList(params);
	}

	/**
	 * 查询扩展分页数据. <br>
	 *
	 * @param params 查询条件
	 * @param pageable   分页信息，传空查询所有
	 * @return 扩展分页数据
	 * @throws ServiceException
	 */
	default IPage<T> selectExtensionPage(Map<?, ?> params, IPageable pageable) throws ServiceException {
		IPage<T> page = new IPage<T>(pageable);
		if (pageable == null) {// 查询所有
			int totalCount = selectExtensionCount(params);
			page.setTotalCount(totalCount);
			if (totalCount <= 0)
				return page;
			page.setList(selectExtensionList(params));
		} else {// 分页查询
			Page<T> selectPage = (Page<T>) selectExtensionList(params, pageable);
			if (selectPage != null) {
				page.setTotalCount(selectPage.getTotal());
				page.setList(selectPage.getResult());
			}
		}
		return page;
	}

	/**
	 * 是否存在指定条件的记录
	 *
	 * @param params 查询条件
	 * @return 是否存在记录
	 */
	default boolean exists(Map<?, ?> params) {
		return selectExtensionCount(params) > 0;
	}

}
