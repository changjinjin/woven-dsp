package com.info.baymax.common.persistence.entity.base;

import com.info.baymax.common.persistence.entity.preprocess.PreEntityService;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.persistence.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.persistence.service.EnabledService;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;

import java.io.Serializable;
import java.util.List;

public interface BaseCommonEntityService<ID extends Serializable, T extends CommonEntity<ID>>
    extends BaseOwnerEntityService<ID, T>, BaseIdableAndExampleQueryService<ID, T>, EnabledService<ID, Integer, T>,
    PreEntityService<T> {

    public static final String PROPERTY_NAME = "name";
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

    default List<T> findAllByTenantIdAndName(String tenantId, String name) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(
                FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name)));
    }

    default T findOneByName(String tenantId, String owner, String name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId)
                .andEqualTo(PROPERTY_OWNER, owner).andEqualTo(PROPERTY_NAME, name)));
    }

    default boolean existsByTenantIdAndName(String tenantId, String name) {
        Integer count = selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(
                FieldGroup.builder().andEqualTo(PROPERTY_TENANTID, tenantId).andEqualTo(PROPERTY_NAME, name)));
        return count > 0;
    }
}
