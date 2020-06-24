package com.info.baymax.common.service.criteria.field;

import com.info.baymax.common.service.criteria.field.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.query.QueryBuilder;

import java.util.List;

/**
 * 查询条件组构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月6日 上午11:41:02
 */
@SuppressWarnings("unchecked")
public interface FieldGroupBuilder<T extends QueryBuilder<T>, B extends FieldGroupBuilder<T, B>>
    extends FieldBuilder<B> {

    /**
     * 并行条件之间的逻辑关系，默认AND
     *
     * @param andOr 逻辑关系
     * @return this builder
     */
    B andOr(AndOr andOr);

    /**
     * 添加一组条件
     *
     * @param fields 条件集合
     * @return this builder
     */
    B fields(List<Field> fields);

    /**
     * 删除一组条件
     *
     * @param fields 条件集合
     * @return this builder
     */
    B removeFields(List<Field> fields);

    /**
     * 添加一组条件
     *
     * @param fields 条件集合
     * @return this builder
     */
    B fields(Field... fields);

    /**
     * 删除一组条件
     *
     * @param fields 条件集合
     * @return this builder
     */
    B removeFields(Field... fields);

    /**
     * 删除一组条件
     *
     * @param fieldNames 删除条件名称
     * @return this builder
     */
    B removeFields(String... fieldNames);

    /**
     * 添加一个条件
     *
     * @param field 条件
     * @return this builder
     */
    B field(Field field);

    /**
     * 删除一个条件
     *
     * @param field 条件
     * @return this builder
     */
    B removeField(Field field);

    /**
     * 根据条件名称删除一个条件
     *
     * @param fieldName 删除属性名称
     * @return this builder
     */
    B removeField(String fieldName);

    /**
     * 添加一个group
     *
     * @param group group对象
     * @return this builder
     */
    B group(FieldGroup<T> group);

    /**
     * 添加一个and group
     *
     * @param group group对象
     * @return this builder
     */
    B andGroup(FieldGroup<T> group);

    /**
     * 添加一个or group
     *
     * @param group group对象
     * @return this builder
     */
    B orGroup(FieldGroup<T> group);

    /**
     * 添加一个or group
     *
     * @param group group对象
     * @return this builder
     */
    B removeGroup(FieldGroup<T> group);

    /**
     * 批量删除 group
     *
     * @param groups groups集合
     * @return this builder
     */
    B removeGroups(FieldGroup<T>... groups);

    /**
     * 批量删除 group
     *
     * @param groups groups集合
     * @return this builder
     */
    B removeGroups(List<FieldGroup<T>> groups);

    /**
     * 根据条件节点顺序重新设置节点索引
     *
     * @return this builder
     */
    B reIndex();

}
