package com.info.baymax.common.entity.base;

/**
 * 继承{@link BaseEntity}的实体类公共查询接口定义
 *
 * @param <T> 继承BaseEntity的实体类型
 * @author jingwei.yang
 * @date 2019年7月2日 上午10:20:47
 */
public interface BaseEntityService<T extends BaseEntity> extends CommonEntityService<Long, T> {
}
