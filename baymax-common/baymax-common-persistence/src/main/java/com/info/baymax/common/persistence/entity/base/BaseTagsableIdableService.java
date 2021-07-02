package com.info.baymax.common.persistence.entity.base;

import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.persistence.entity.gene.Idable;

import java.io.Serializable;
import java.util.List;

public interface BaseTagsableIdableService<ID extends Serializable, T extends Idable<ID> & Tagsable> {

	/**
	 * 根据tag搜索列表
	 *
	 * @param tag
	 * @return 数据列表
	 */
	default List<T> selectListByTag(String tag) {
		return null;
	}

	/**
	 * 根据tag搜索分页查询
	 *
	 * @param tag 标签名称
	 * @return 数据分页
	 */
	default IPage<T> selectPageByTag(String tag) {
		return null;
	}

}
