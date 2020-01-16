package com.info.baymax.common.entity.base;

import com.info.baymax.common.service.criteria.example.ExampleQuery;

import java.util.List;

/**
 * 继承{@link com.info.baymax.dsp.data.dataset.entity.Maintable}的实体类公共查询接口定义
 * 
 * @author jingwei.yang
 * @date 2019年10月9日 上午11:06:23
 * @param <T> 继承Maintable的类型
 */
public interface BaseMaintableService<T extends Maintable> extends CommonEntityService<String, T> {

	default int countExpired(Long expireTime) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
				.end());
	}

	default List<T> findExpired(Long expireTime) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
				.end());
	}

	default int deleteExpired(Long expireTime) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
				.end());
	}

}
