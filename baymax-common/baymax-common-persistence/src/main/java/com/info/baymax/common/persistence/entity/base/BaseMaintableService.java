package com.info.baymax.common.persistence.entity.base;

import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import com.info.baymax.common.queryapi.query.field.FieldGroup;

import java.util.List;

/**
 * 继承{@link com.info.baymax.dsp.data.dataset.entity.Maintable}的实体类公共查询接口定义
 *
 * @param <T> 继承Maintable的类型
 * @author jingwei.yang
 * @date 2019年10月9日 上午11:06:23
 */
public interface BaseMaintableService<T extends Maintable> extends BaseCommonEntityService<String, T> {

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_EXPIREDTIME = "expiredTime";

    default int countExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
            ));
    }

    default List<T> findExpired(Long expireTime, int batchSize) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
            ).offset(0, batchSize));
    }

    default int deleteExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
            ));
    }

    default T findOneByName(String tenantId, String name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andEqualTo(PROPERTY_TENANTID, tenantId)//
                .andEqualTo(PROPERTY_NAME, name)//
            ));
    }

    default T findOneByName(String tenantId, String owner, String name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andEqualTo(PROPERTY_TENANTID, tenantId)//
                .andEqualTo(PROPERTY_OWNER, owner)//
                .andEqualTo(PROPERTY_NAME, name)//
            ));
    }

    default boolean existsByTenantIdAndName(String tenantId, String name) {
        Integer count = selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder()//
                .andEqualTo(PROPERTY_TENANTID, tenantId)//
                .andEqualTo(PROPERTY_NAME, name)//
            ));
        return count > 0;
    }
}
