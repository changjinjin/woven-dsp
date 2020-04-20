package com.info.baymax.common.service.criteria;

/**
 * 条件建造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月6日 上午11:41:41
 */
public interface FieldBuilder<B extends FieldBuilder<B>> {

    /**
     * 条件: AND ${property} IS NULL
     *
     * @param property 属性名
     * @return 建造器
     */
    B andIsNull(String property);

    /**
     * 条件: AND ${property} IS NOT NULL
     *
     * @param property 属性名
     * @return 建造器
     */
    B andIsNotNull(String property);

    /**
     * 条件: AND ${property} = ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andEqualTo(String property, Object value);

    /**
     * value非空执行条件: AND ${property} = ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andEqualToIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} <> ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotEqualTo(String property, Object value);

    /**
     * value非空执行条件: AND ${property} <> ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotEqualToIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} > ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andGreaterThan(String property, Object value);

    /**
     * value非空执行条件: AND ${property} > ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andGreaterThanIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} >= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andGreaterThanOrEqualTo(String property, Object value);

    /**
     * value非空执行条件: AND ${property} >= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andGreaterThanOrEqualToIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} < ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLessThan(String property, Object value);

    /**
     * value非空执行条件: AND ${property} < ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLessThanIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} <= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLessThanOrEqualTo(String property, Object value);

    /**
     * value非空执行条件: AND ${property} <= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLessThanOrEqualToIfNotNull(String property, Object value);

    /**
     * 条件: AND ${property} IN ${values}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B andIn(String property, Object[] values);

    /**
     * values非空执行条件: AND ${property} IN ${values}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B andInIfNotEmpty(String property, Object[] values);

    /**
     * 条件: AND ${property} NOT IN ${values}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B andNotIn(String property, Object[] values);

    /**
     * value非空执行条件: AND ${property} NOT IN ${values}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B andNotInIfNotEmpty(String property, Object[] values);

    /**
     * 条件: AND ${property} BETWEEN ${value1} AND ${value2}
     *
     * @param property 属性名
     * @param value1   属性值1
     * @param value2   属性值2
     * @return 建造器
     */
    B andBetween(String property, Object value1, Object value2);

    /**
     * 条件: AND ${property} NOT BETWEEN ${value1} AND ${value2}
     *
     * @param property 属性名
     * @param value1   属性值1
     * @param value2   属性值2
     * @return 建造器
     */
    B andNotBetween(String property, Object value1, Object value2);

    /**
     * 条件: AND ${property} LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLeftLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andLeftLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andRightLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andRightLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andFullLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andFullLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} NOT LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} NOT LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} NOT LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotLeftLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} NOT LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotLeftLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} NOT LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotRightLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} NOT LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotRightLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${property} NOT LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotFullLike(String property, String value);

    /**
     * value非空执行条件: AND ${property} NOT LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B andNotFullLikeIfNotNull(String property, String value);

    /**
     * 条件: AND ${param.property1} = ${param.value1} AND ${param.property2} = ${param.value2}
     *
     * @param param 属性名属性值列表
     * @return 建造器
     */
    B andEqualTo(Object param);

    /**
     * 条件: AND ${param.property1} = ${param.value1} AND ${param.property2} = ${param.value2}
     *
     * @param param 属性名属性值列表
     * @return 建造器
     */
    B andAllEqualTo(Object param);

    /**
     * 条件: OR ${property} IS NULL
     *
     * @param property 属性名
     * @return 建造器
     */
    B orIsNull(String property);

    /**
     * 条件: OR ${property} IS NOT NULL
     *
     * @param property 属性名
     * @return 建造器
     */
    B orIsNotNull(String property);

    /**
     * 条件: OR ${property} = ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orEqualTo(String property, Object value);

    /**
     * value非空执行条件: OR ${property} = ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orEqualToIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} <> ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotEqualTo(String property, Object value);

    /**
     * value非空执行条件: OR ${property} <> ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotEqualToIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} > ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orGreaterThan(String property, Object value);

    /**
     * value非空执行条件: OR ${property} > ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orGreaterThanIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} >= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orGreaterThanOrEqualTo(String property, Object value);

    /**
     * value非空执行条件: OR ${property} >= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orGreaterThanOrEqualToIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} < ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLessThan(String property, Object value);

    /**
     * value非空执行条件: OR ${property} < ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLessThanIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} <= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLessThanOrEqualTo(String property, Object value);

    /**
     * value非空执行条件: OR ${property} <= ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLessThanOrEqualToIfNotNull(String property, Object value);

    /**
     * 条件: OR ${property} IN ${value}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B orIn(String property, Object[] values);

    /**
     * value非空执行条件: OR ${property} IN ${value}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B orInIfNotEmpty(String property, Object[] values);

    /**
     * 条件: OR ${property} NOT IN ${value}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B orNotIn(String property, Object[] values);

    /**
     * value非空执行条件: OR ${property} NOT IN ${value}
     *
     * @param property 属性名
     * @param values   属性值
     * @return 建造器
     */
    B orNotInIfNotEmpty(String property, Object[] values);

    /**
     * 条件: OR ${property} BETWEEN ${value1} AND ${value2}
     *
     * @param property 属性名
     * @param value1   属性值1
     * @param value2   属性值2
     * @return 建造器
     */
    B orBetween(String property, Object value1, Object value2);

    /**
     * 条件: OR ${property} NOT BETWEEN ${value1} AND ${value2}
     *
     * @param property 属性名
     * @param value1   属性值1
     * @param value2   属性值2
     * @return 建造器
     */
    B orNotBetween(String property, Object value1, Object value2);

    /**
     * 条件: OR ${property} LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLeftLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orLeftLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orRightLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orRightLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orFullLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orFullLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} NOT LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} NOT LIKE ${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotLikeifNotNull(String property, String value);

    /**
     * 条件: OR ${property} NOT LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotLeftLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} NOT LIKE ${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotLeftLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} NOT LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotRightLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} NOT LIKE '%'|${value}
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotRightLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${property} NOT LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotFullLike(String property, String value);

    /**
     * value非空执行条件: OR ${property} NOT LIKE '%'|${value}|'%'
     *
     * @param property 属性名
     * @param value    属性值
     * @return 建造器
     */
    B orNotFullLikeIfNotNull(String property, String value);

    /**
     * 条件: OR ${param.property1} = ${param.value1} AND ${param.property2} = ${param.value2} ...
     *
     * @param param 属性名属性值列表
     * @return 建造器
     */
    B orEqualTo(Object param);

    /**
     * 条件: OR ${param.property1} = ${param.value1} AND ${param.property2} = ${param.value2} ...
     *
     * @param param 属性名属性值列表
     * @return 建造器
     */
    B orAllEqualTo(Object param);

}
