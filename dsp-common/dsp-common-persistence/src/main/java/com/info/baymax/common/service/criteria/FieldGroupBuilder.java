package com.info.baymax.common.service.criteria;

import com.info.baymax.common.service.criteria.example.Field;
import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.common.service.criteria.example.SqlEnums.AndOr;

import java.util.List;

/**
 * 查询条件组构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月6日 上午11:41:02
 */
public interface FieldGroupBuilder<B extends FieldGroupBuilder<B>> extends FieldBuilder<B> {

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
     * @param fields
     * @return this builder
     */
    FieldGroup fields(List<Field> fields);

    /**
     * 添加一组条件
     *
     * @param fields
     * @return
     */
    FieldGroup fields(Field... fields);

    /**
     * 添加一个条件
     *
     * @param field
     * @return this builder
     */
    FieldGroup field(Field field);

    /**
     * 添加一个group
     *
     * @param group group对象
     * @return this builder
     */
    B group(FieldGroup group);

    /**
     * 添加一个and group
     *
     * @param group group对象
     * @return this builder
     */
    B andGroup(FieldGroup group);

    /**
     * 添加一个or group
     *
     * @param group group对象
     * @return this builder
     */
    B orGroup(FieldGroup group);

}
