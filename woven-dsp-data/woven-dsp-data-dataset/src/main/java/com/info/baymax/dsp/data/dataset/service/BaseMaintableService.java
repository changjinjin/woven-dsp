package com.info.baymax.dsp.data.dataset.service;

import com.info.baymax.common.entity.preprocess.PreEntityService;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.service.BaseIdableService;
import com.info.baymax.common.service.criteria.ExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.Maintable;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 继承{@link com.info.baymax.dsp.data.dataset.entity.Maintable}的实体类公共查询接口定义
 * 
 * @author jingwei.yang
 * @date 2019年10月9日 上午11:06:23
 * @param <T> 继承Maintable的类型
 */
public interface BaseMaintableService<T extends Maintable>
		extends ExampleQueryService<T>, BaseIdableService<T>, PreEntityService<T> {

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_OWNER = "owner";
	public static final String PROPERTY_TENANTID = "tenantId";
	public static final String PROPERTY_EXPIREDTIME = "expiredTime";

	@Override
	default BaseExampleMapper<T> getBaseExampleMapper() {
		return getMyIdableMapper();
	}

	@Override
	default void preInsert(T t) {
		t.preInsert();
	}

	@Override
	default void preUpdate(T t) {
		t.preUpdate();
	}

	/**
	 * 保存或者更新
	 *
	 * @param t
	 * @return
	 */
	default T saveOrUpdate(T t) {
		if (StringUtils.isNotEmpty(t.getId()) && existsWithPrimaryKey(t.getId())) {
			updateByPrimaryKeySelective(t);
		} else {
			if (StringUtils.isEmpty(t.getId())) {
				t.setId(null);
			}
			insertSelective(t);
		}
		return selectByPrimaryKey(t.getId());
	}

	default T findOne(String tenantId, String id) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_ID, id)//
				.end());
	}

	default List<T> findByIds(String tenantId, Object[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andIn(PROPERTY_ID, ids)//
				.end());
	}

	default List<T> findByIds(String tenantId, String owner, Object[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.andIn(PROPERTY_ID, ids)//
				.end());
	}

	default List<T> findAllByTenantId(String tenantId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.end());
	}

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

	default List<T> findAllByOwnerId(String tenantId, String owner) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.end());
	}

	default int delete(String tenantId, String id) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_ID, id)//
				.end());
	}

	default int delete(String tenantId, String owner, String id) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.andEqualTo(PROPERTY_ID, id)//
				.end());
	}

	default int deleteByIds(String tenantId, Object[] ids) {
		// 分批删除，避免“ORA-01795: 列表中的最大表达式数为1000”的错误
		if (ids != null) {
			List<Object[]> groupArr = ICollections.groupArr(ids, 999);
			for (Object[] objects : groupArr) {
				delete(ExampleQuery.builder(getEntityClass())//
						.fieldGroup()//
						.andEqualTo(PROPERTY_TENANTID, tenantId)//
						.andIn(PROPERTY_ID, objects)//
						.end());
			}
			return ids.length;
		}
		return 0;
	}

	default int deleteByIds(String tenantId, String owner, Object[] ids) {

		// 分批删除，避免“ORA-01795: 列表中的最大表达式数为1000”的错误
		if (ids != null) {
			List<Object[]> groupArr = ICollections.groupArr(ids, 999);
			for (Object[] objects : groupArr) {
				delete(ExampleQuery.builder(getEntityClass())//
						.fieldGroup()//
						.andEqualTo(PROPERTY_TENANTID, tenantId)//
						.andEqualTo(PROPERTY_OWNER, owner)//
						.andIn(PROPERTY_ID, objects)//
						.end());
			}
			return ids.length;
		}
		return 0;
	}

	default List<T> findAll(String tenantId, Object[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andIn(PROPERTY_ID, ids)//
				.end());
	}

	default int count(String tenantId) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.end());
	}

	default T findOneByName(String tenantId, String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_NAME, name)//
				.end());
	}

	default T findOneByName(String tenantId, String owner, String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.andEqualTo(PROPERTY_NAME, name)//
				.end());
	}

	default int deleteByOwner(String owner) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.end());
	}

	default boolean existsByTenantIdAndName(String tenantId, String name) {
		Integer count = selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_NAME, name)//
				.end());
		return count > 0;
	}
}
