package com.info.baymax.common.persistence.entity.base;

import com.info.baymax.common.persistence.entity.preprocess.PreEntityService;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;

import java.io.Serializable;
import java.util.List;

/**
 * 继承{@link com.merce.woven.data.entity.Maintable}的实体类公共查询接口定义
 *
 * @param <T> 继承Maintable的类型
 * @author jingwei.yang
 * @date 2019年10月9日 上午11:06:23
 */
public interface BaseOwnerEntityService<ID extends Serializable, T extends OwnerEntity<ID>>
	extends BaseIdableAndExampleQueryService<ID, T>, PreEntityService<T> {

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_OWNER = "owner";
	public static final String PROPERTY_TENANTID = "tenantId";

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

	default T findOne(String tenantId, ID id) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_ID, id)//
			));
	}

	default List<T> findByIds(String tenantId, ID[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andIn(PROPERTY_ID, ids)//
			));
	}

	default List<T> findByIds(String tenantId, String owner, ID[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.andIn(PROPERTY_ID, ids)//
			));
	}

	default List<T> findAllByTenantId(String tenantId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
			));
	}

	default List<T> findAllByOwnerId(String tenantId, String owner) {
		return selectList(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
			));
	}

	default int delete(String tenantId, ID id) {
		return delete(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_ID, id)//
			));
	}

	default int delete(String tenantId, String owner, ID id) {
		return delete(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andEqualTo(PROPERTY_OWNER, owner)//
				.andEqualTo(PROPERTY_ID, id)//
			));
	}

	default int deleteByIds(String tenantId, ID[] ids) {
		// 分批删除，避免“ORA-01795: 列表中的最大表达式数为1000”的错误
		if (ids != null) {
			List<Object[]> groupArr = ICollections.groupArr(ids, 999);
			for (Object[] objects : groupArr) {
				delete(ExampleQuery.builder(getEntityClass())//
					.fieldGroup(FieldGroup.builder()//
						.andEqualTo(PROPERTY_TENANTID, tenantId)//
						.andIn(PROPERTY_ID, objects)//
					));
			}
			return ids.length;
		}
		return 0;
	}

	default int deleteByIds(String tenantId, String owner, ID[] ids) {

		// 分批删除，避免“ORA-01795: 列表中的最大表达式数为1000”的错误
		if (ids != null) {
			List<Object[]> groupArr = ICollections.groupArr(ids, 999);
			for (Object[] objects : groupArr) {
				delete(ExampleQuery.builder(getEntityClass())//
					.fieldGroup(FieldGroup.builder()//
						.andEqualTo(PROPERTY_TENANTID, tenantId)//
						.andEqualTo(PROPERTY_OWNER, owner)//
						.andIn(PROPERTY_ID, objects)//
					));
			}
			return ids.length;
		}
		return 0;
	}

	default List<T> findAll(String tenantId, ID[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
				.andIn(PROPERTY_ID, ids)//
			));
	}

	default int count(String tenantId) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_TENANTID, tenantId)//
			));
	}

	default int deleteByOwner(String owner) {
		return delete(ExampleQuery.builder(getEntityClass())//
			.fieldGroup(FieldGroup.builder()//
				.andEqualTo(PROPERTY_OWNER, owner)//
			));
	}
}
