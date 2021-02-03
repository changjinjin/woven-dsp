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
public interface BaseMaintableService<T extends Maintable> extends CommonEntityService<String, T> {

    default int countExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)));
    }

    default List<T> findExpired(Long expireTime) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)));
    }

    default int deleteExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)));
    }

}
