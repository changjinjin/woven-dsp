package com.info.baymax.common.service.entity;

import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/**
 * 获取类参数中泛型的实体类型
 *
 * @author jingwei.yang
 * @date 2019年7月1日 下午8:11:00
 */
public interface EntityClassService<T> {

    /**
     * 返回实现类参数的第一个参数类型
     *
     * @return 实现类参数的第一个参数类型
     */
    Class<T> getEntityClass();

    /**
     * 获取表名称，只有数据库映射的类型才可以拿到表名，否则拿不到
     *
     * @return 表名称
     */
    default String getTableName() {
        EntityTable entityTable = null;
        try {
            entityTable = EntityHelper.getEntityTable(getEntityClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityTable == null ? null : entityTable.getName();
    }
}
