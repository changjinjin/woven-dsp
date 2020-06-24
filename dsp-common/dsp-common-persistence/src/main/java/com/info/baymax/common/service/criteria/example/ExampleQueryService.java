package com.info.baymax.common.service.criteria.example;

import com.info.baymax.common.entity.preprocess.annotation.PreUpdate;
import com.info.baymax.common.mybatis.mapper.aggregation.AggregateCondition;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.BaseExampleService;
import com.info.baymax.common.service.BasePreprocessService;
import com.info.baymax.common.service.entity.EntityClassService;

import java.util.List;

/**
 * 通过Example组合查询数据的接口定义，用于复杂动态条件查询
 *
 * @author jingwei.yang
 * @date 2019年5月7日 上午10:11:38
 */
public interface ExampleQueryService<T> extends BaseExampleService<T>, EntityClassService<T>, BasePreprocessService<T> {

    /**
     * 根据查询条件获取记录条数
     *
     * @param query 查询条件
     * @return 查询的记录条数
     */
    default int selectCount(ExampleQuery query) {
        return selectCountByExample(ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 查询是否存在符合条件的记录
     *
     * @param query 查询条件
     * @return 如果存在返回true，否则返回false
     */
    default boolean exists(ExampleQuery query) {
        return selectCount(query) > 0;
    }

    /**
     * 根据查询条件获取数据集
     *
     * @param query 查询条件
     * @return 查询的结果集
     */
    default List<T> selectList(ExampleQuery query) {
        IPageable pageable = query.getPageable();
        if (pageable.isPageable()) {
            return selectByExampleAndPage(ExampleHelper.createExample(query, getEntityClass()), pageable);
        }
        return selectByExample(ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 根据查询条件获取指定条数的数据集
     *
     * @param query 查询条件
     * @param limit 查询条数
     * @return 查询的结果集
     */
    default List<T> selectListAndLimit(ExampleQuery query, int limit) {
        if (limit > 0)
            query.offset(0, limit);
        return selectList(query);
    }

    /**
     * 根据动态查询分页查询
     *
     * @param query 查询条件
     * @return 分页数据集
     */
    default IPage<T> selectPage(ExampleQuery query) {
        return selectPageByExample(ExampleHelper.createExample(query, getEntityClass()), query.getPageable());
    }

    /**
     * 根据查询条件获取单条记录
     *
     * @param query 查询条件
     * @return 查询的单条结果
     */
    default T selectOne(ExampleQuery query) {
        return selectOneByExample(ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 根据条件删除数据
     *
     * @param query 删除条件
     */
    default int delete(ExampleQuery query) {
        return deleteByExample(ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 根据条件更新数据（更新全部字段，即参数record中包含的所有属性）
     *
     * @param record 需要更新的属性列表包装对象
     * @param query  更新的记录匹配条件
     * @return 更新的数据条数
     */
    default int updateByExample(@PreUpdate T record, ExampleQuery query) {
        preUpdate(record);
        return updateByExample(record, ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 根据条件更新数据（更新选择的字段，即参数record中不为空的属性）
     *
     * @param record 需要更新的属性列表包装对象
     * @param query  更新的记录匹配条件
     * @return 更新的数据条数
     */
    default int updateByExampleSelective(@PreUpdate T record, ExampleQuery query) {
        preUpdate(record);
        return updateByExampleSelective(record, ExampleHelper.createExample(query, getEntityClass()));
    }

    /**
     * 聚合查询
     *
     * @param query              查询条件
     * @param aggregateCondition 聚合条件
     * @return 查询结果
     */
    default List<T> selectAggregationByQuery(ExampleQuery query, AggregateCondition aggregateCondition) {
        return selectAggregationByExample(ExampleHelper.createExample(query), aggregateCondition);
    }

}
