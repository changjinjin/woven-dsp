package com.info.baymax.common.entity.base;

import java.io.Serializable;
import java.util.List;

import com.info.baymax.common.entity.preprocess.PreEntityService;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;

public interface CommonEntityService<ID extends Serializable, T extends CommonEntity<ID>>
		extends BaseIdableAndExampleQueryService<ID, T>, PreEntityService<T> {

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

	default T findOne(String tenantId, ID id) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_ID, id)));
	}

	default List<T> findByIds(String tenantId, ID[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andIn(PROPERTY_ID, ids)));
	}

	default T findOne(ID id) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_ID, id)));
	}

	default List<T> findAllByTenantId(String tenantId) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.offset(0, Integer.MAX_VALUE)//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)));
	}

	default List<T> findAllByTenantIdAndName(String tenantId, String name) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(
						FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name)));
	}

	default List<T> findAllByTenantIdAndOwner(String tenantId, String owner) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_OWNER,
						owner)));
	}

	default int delete(String tenantId, ID id) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_ID, id)));
	}

	default int delete(String tenantId, String owner, ID id) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
						.andEqualTo(PROPERTY_OWNER, owner).andEqualTo(PROPERTY_ID, id)));
	}

	default int deleteByIds(String tenantId, ID[] ids) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andIn(PROPERTY_ID, ids)));
	}

	default int deleteByIds(String tenantId, String owner, ID[] ids) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
						.andEqualTo(PROPERTY_OWNER, owner).andIn(PROPERTY_ID, ids)));
	}

	default List<T> findAll(String tenantId, ID[] ids) {
		return selectList(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andIn(PROPERTY_ID, ids)));
	}

	default int count(String tenantId) {
		return selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)));
	}

	default T findOneByName(String tenantId, String owner, String name) {
		return selectOne(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
						.andEqualTo(PROPERTY_OWNER, owner).andEqualTo(PROPERTY_NAME, name)));
	}

	default int deleteByOwner(String owner) {
		return delete(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_OWNER, owner)));
	}

	default boolean existsByTenantIdAndName(String tenantId, String name) {
		Integer count = selectCount(ExampleQuery.builder(getEntityClass())//
				.fieldGroup(
						FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name)));
		return count > 0;
	}
}
