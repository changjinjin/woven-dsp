package com.info.baymax.common.persistence.service;

import com.github.pagehelper.Page;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.page.IPageable;
import com.info.baymax.common.persistence.mybatis.mapper.aggregation.AggregateCondition;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.persistence.mybatis.mapper.example.Example;
import com.info.baymax.common.persistence.service.criteria.example.ExampleHelper;
import com.info.baymax.common.persistence.service.criteria.example.ExampleQuery;
import org.apache.ibatis.session.RowBounds;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * 根据Example查询
 *
 * @author jingwei.yang
 * @date 2019年6月25日 下午5:14:36
 */
@Transactional
public interface BaseExampleService<T> extends BasePageService<T>, BaseExampleMapper<T>, BasePreprocessService<T> {

    BaseExampleMapper<T> getBaseExampleMapper();


    @Override
    default int updateByExample(T record, Example example) {
        preUpdate(record);
        return getBaseExampleMapper().updateByExample(record, example);
    }


    @Override
    default int updateByExampleSelective(T record, Example example) {
        preUpdate(record);
        return getBaseExampleMapper().updateByExampleSelective(record, example);
    }

    @Override
    default int deleteByExample(Example example) {
        if (example == null) {
            throw new ParameterCheckException("根据条件批量删除数据时参数recordList不能为空");
        }
        return getBaseExampleMapper().deleteByExample(example);
    }

    /**
     * 查询所有，不处理分页
     */
    @Override
    default List<T> selectByExample(Example example) {
        return getBaseExampleMapper().selectByExample(example);
    }

    @Override
    default T selectOneByExample(Example example) {
        return getBaseExampleMapper().selectOneByExample(example);
    }

    @Override
    default int selectCountByExample(Example example) {
        return getBaseExampleMapper().selectCountByExample(example);
    }

    @Override
    default List<T> selectByExampleAndRowBounds(Example example, RowBounds rowBounds) {
        return getBaseExampleMapper().selectByExampleAndRowBounds(example, rowBounds);
    }

    /**
     * 使用example对象查询分页数据
     *
     * @param example 查询条件
     * @param limit   查询数据条数
     * @return 查询结果集合
     */
    default List<T> selectByExampleAndLimit(Example example, int limit) {
        if (limit < 0)
            return null;
        startPage(IPageable.offset(0, limit));
        return selectByExample(example);
    }

    /**
     * 使用example对象查询分页数据
     *
     * @param example  查询条件
     * @param pageable 分页信息
     * @return 查询结果集合
     */
    default List<T> selectByExampleAndPage(Example example, IPageable pageable) {
        startPage(pageable);
        return selectByExample(example);
    }

    /**
     * 使用example对象查询分页数据
     *
     * @param example  查询条件
     * @param pageable 分页信息
     * @return 查询结果，使用分页对象包装
     */
    default IPage<T> selectPageByExample(Example example, IPageable pageable) {
        IPage<T> page = IPage.<T>of(pageable);
        if (pageable == null || !pageable.isPageable()) {// 查询所有
            int totalCount = selectCountByExample(example);
            page.setPageNum(1);
            page.setPageSize(totalCount);
            page.setTotalCount(totalCount);
            if (totalCount <= 0)
                return page;

            page.setList(selectByExample(example));
        } else {// 分页查询
            Page<T> selectPage = (Page<T>) selectByExampleAndPage(example, pageable);
            if (selectPage != null) {
                page.setTotalCount(selectPage.getTotal());
                page.setList(selectPage.getResult());
            }
        }
        return page;
    }

    @Override
    default List<Map<String, Object>> selectMapByExample(Example example) {
        return getBaseExampleMapper().selectMapByExample(example);
    }

    @Override
    default Map<String, Object> selectOneMapByExample(Example example) {
        return getBaseExampleMapper().selectOneMapByExample(example);
    }

    default List<Map<String, Object>> selectMapByExampleAndPage(Example example, IPageable pageable) {
        startPage(pageable);
        return selectMapByExample(example);
    }

    default IPage<Map<String, Object>> selectMapPageByExample(Example example, IPageable pageable) {
        IPage<Map<String, Object>> page = IPage.<Map<String, Object>>of(pageable);
        if (pageable == null || !pageable.isPageable()) {// 查询所有
            int totalCount = selectCountByExample(example);
            page.setPageNum(1);
            page.setPageSize(totalCount);
            page.setTotalCount(totalCount);
            if (totalCount <= 0)
                return page;

            page.setList(selectMapByExample(example));
        } else {// 分页查询
            Page<Map<String, Object>> selectPage = (Page<Map<String, Object>>) selectMapByExampleAndPage(example,
                pageable);
            if (selectPage != null) {
                page.setTotalCount(selectPage.getTotal());
                page.setList(selectPage.getResult());
            }
        }
        return page;
    }

    @Override
    default List<T> selectAggregationByExample(Example example, AggregateCondition aggregateCondition) {
        return getBaseExampleMapper().selectAggregationByExample(example, aggregateCondition);
    }

    default List<T> selectAggregationByExample(ExampleQuery query, AggregateCondition aggregateCondition) {
        return getBaseExampleMapper().selectAggregationByExample(ExampleHelper.createExample(query), aggregateCondition);
    }
}
