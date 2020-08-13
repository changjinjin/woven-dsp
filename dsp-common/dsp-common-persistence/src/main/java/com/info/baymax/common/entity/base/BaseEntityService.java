package com.info.baymax.common.entity.base;

import java.util.List;

import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.service.criteria.example.ExampleQuery;

/**
 * 继承{@link BaseEntity}的实体类公共查询接口定义
 *
 * @param <T> 继承BaseEntity的实体类型
 * @author jingwei.yang
 * @date 2019年7月2日 上午10:20:47
 */
public interface BaseEntityService<T extends BaseEntity> extends CommonEntityService<Long, T> {
    default int countExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
                .andNotEqualTo(PROPERTY_EXPIREDTIME, 0)));
    }

    default List<T> findExpired(Long expireTime) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
                .andNotEqualTo(PROPERTY_EXPIREDTIME, 0)));
    }

    default int deleteExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andLessThan(PROPERTY_EXPIREDTIME, expireTime)));
    }
}
