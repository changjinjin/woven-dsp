package com.info.baymax.common.service.criteria;

import com.info.baymax.common.service.criteria.example.FieldGroup;
import com.info.baymax.common.service.criteria.example.Sort;
import com.info.baymax.common.mybatis.page.IPageable;

/**
 * 查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface QueryBuilder<B extends QueryBuilder<B>> {

    /**
     * 关闭分页
     *
     * @return this builder
     */
    B unpaged();

    /**
     * 开启分页
     *
     * @return this builder
     */
    B paged();

    /**
     * 设置分页信息
     *
     * @return this builder
     */
    B pageable(IPageable pageable);

    /**
     * 指定分页当前页码
     *
     * @param pageNum 页码
     * @return this builder
     */
    B pageNum(int pageNum);

    /**
     * 指定分页页长
     *
     * @param pageSize 页长
     * @return this builder
     */
    B pageSize(int pageSize);

    /**
     * 分页
     *
     * @param pageNum  页码
     * @param pageSize 页长
     * @return this builder
     */
    B page(int pageNum, int pageSize);

    /**
     * 分页
     *
     * @param offset 偏移量
     * @param limit  页长
     * @return this builder
     */
    B offset(int offset, int limit);

    /**
     * 是否去重查询，一般无需指定，默认不去重
     *
     * @param distinct 是否去重，默认false
     * @return this builder
     */
    B distinct(boolean distinct);

    /**
     * 是否锁表查询，一般无需指定
     *
     * @param forUpdate 是否锁表，默认false
     * @return this builder
     */
    B forUpdate(boolean forUpdate);

    /**
     * 开启动态sql查询
     *
     * @return 开启动态sql查询
     */
    B dynamic(String dynamicTable);

    /**
     * 添加追加sql
     *
     * @return 添加追加sql
     */
    B append(String appendTable);

    /**
     * 表别名
     *
     * @return 添加表别名
     */
    B tableAlias(String tableAlias);

    /**
     * 指定计数字段，是有统计技术的查询才有用，默认不需指定
     *
     * @param countProperty 计数字段
     * @return this builder
     */
    B countProperty(String countProperty);

    /**
     * 指定需要查询的字段列表
     *
     * @param selectProperties 需要查询的字段列表
     * @return this builder
     */
    B selectProperties(String... selectProperties);

    /**
     * 指定需要排除的查询字段
     *
     * @param excludeProperties 排除的字段列表
     * @return this builder
     */
    B excludeProperties(String... excludeProperties);

    /**
     * 添加一个组合查询条件对象，默认根节点
     *
     * @return this builder
     */
    FieldGroup fieldGroup();

    /**
     * 添加一个组合查询条件对象
     *
     * @param fieldGroup 组合查询条件对象
     * @return this builder
     */
    B fieldGroup(FieldGroup fieldGroup);

    /**
     * 添加排序字段
     *
     * @param ordSort 排序属性对象
     * @return this builder
     */
    B sort(Sort ordSort);

    /**
     * 设置多个排序字段
     *
     * @param ordSort 排序属性对象
     * @return this builder
     */
    B sorts(Sort... ordSort);

    /**
     * 添加排序字段，按照字段正序排序
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    B orderBy(String fieldName);

    /**
     * 添加正序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    B orderByAsc(String fieldName);

    /**
     * 添加逆序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    B orderByDesc(String fieldName);
}
