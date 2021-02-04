package com.info.baymax.common.persistence.service;

import com.github.pagehelper.Page;
import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.page.IPage;
import com.info.baymax.common.core.page.IPageable;
import com.info.baymax.common.persistence.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.utils.ICollections;
import org.apache.ibatis.session.RowBounds;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 业务层接口，继承通用方法
 *
 * @author jingwei.yang
 * @date 2019-05-29 14:57
 */
@Transactional
public interface BaseService<T> extends BaseExampleService<T>, MyBaseMapper<T>, BasePreprocessService<T> {

    MyBaseMapper<T> getMyBaseMapper();

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyBaseMapper();
    }


    @Override
    default int insert(T record) {
        if (record != null) {
            preInsert(record);
            return getMyBaseMapper().insert(record);
        }
        return 0;
    }


    @Override
    default int insertSelective(T record) {
        if (record != null) {
            preInsert(record);
            return getMyBaseMapper().insertSelective(record);
        }
        return 0;
    }


    @Override
    default int insertList(List<? extends T> recordList) {
        if (ICollections.hasElements(recordList)) {
            preInsert(recordList);
            return getMyBaseMapper().insertList(recordList);
        }
        return 0;
    }

    /**
     * 批量插入，空字段使用默认值
     *
     * @param recordList 对象集合
     * @return 插入结果
     * @throws ServiceException
     */

    default int insertListSelective(List<T> recordList) throws ServiceException {
        int i = 0;
        if (ICollections.hasElements(recordList)) {
            for (T t : recordList) {
                i += insertSelective(t);
            }
        }
        return i;
    }

    @Override
    default int updateByDiffer(T old, T newer) {
        preUpdate(newer);
        return getMyBaseMapper().updateByDiffer(old, newer);
    }

    @Override
    default int updateByPrimaryKeySelectiveForce(T record, List<String> forceUpdateProperties) {
        preUpdate(record);
        return getMyBaseMapper().updateByPrimaryKeySelectiveForce(record, forceUpdateProperties);
    }

    @Override
    default int delete(T record) {
        if (record == null) {
            throw new ParameterCheckException("根据条件删除数据时参数record不能为空");
        }
        return getMyBaseMapper().delete(record);
    }

    /**
     * 删除所有数据，无条件过滤（慎用），当配置中mapper.safeDelete=true时，该操作无效
     *
     * @return 删除数据条数
     */
    default int deleteAll() {
        return getMyBaseMapper().delete(null);
    }

    @Override
    default int deleteList(List<T> recordList) {
        if (ICollections.hasNoElements(recordList)) {
            throw new ParameterCheckException("根据条件批量删除数据时参数recordList不能为空");
        }
        return getMyBaseMapper().deleteList(recordList);
    }

    @Override
    default T selectOne(T record) {
        return getMyBaseMapper().selectOne(record);
    }

    @Override
    default List<T> select(T record) {
        return getMyBaseMapper().select(record);
    }

    /**
     * 查询并排序
     *
     * @param record        查询条件包装
     * @param orderByClause 排序字段，如：id asc, name desc
     * @return 查询数据集合
     */
    default List<T> select(T record, String orderByClause) {
        startOrderBy(orderByClause);
        return select(record);
    }

    @Override
    default List<T> selectAll() {
        return getMyBaseMapper().selectAll();
    }

    /**
     * 查询所有数据并排序
     *
     * @param orderByClause 排序字段，如：id asc, name desc
     * @return 查询数据集合
     */
    default List<T> selectAll(String orderByClause) {
        startOrderBy(orderByClause);
        return selectAll();
    }

    @Override
    default List<T> selectByRowBounds(T record, RowBounds rowBounds) {
        return getMyBaseMapper().selectByRowBounds(record, rowBounds);
    }

    /**
     * 根据条件查询分页数据
     *
     * @param s        查询条件
     * @param pageable 分页信息
     * @return 查询结果集合
     */
    default List<T> selectByPage(T s, IPageable pageable) {
        startPage(pageable);
        return select(s);
    }

    /**
     * 根据条件查询分页数据
     *
     * @param s        查询条件
     * @param pageable 分页信息
     * @return 查询结果，使用分页对象包装
     */
    default IPage<T> selectPage(T s, IPageable pageable) {
        IPage<T> page = IPage.<T>of(pageable);
        if (pageable == null || !pageable.isPageable()) {// 查询所有
            int totalCount = selectCount(s);
            page.setPageNum(1);
            page.setPageSize(totalCount);
            page.setTotalCount(totalCount);
            if (totalCount <= 0)
                return page;
            page.setList(select(s));
        } else {// 分页查询
            Page<T> selectPage = (Page<T>) selectByPage(s, pageable);
            if (selectPage != null) {
                page.setTotalCount(selectPage.getTotal());
                page.setList(selectPage.getResult());
            }
        }
        return page;
    }

    @Override
    default int selectCount(T record) {
        return getMyBaseMapper().selectCount(record);
    }

    /**
     * 查询所有数据总数，无过滤条件
     *
     * @return 数据总数
     */
    default int selectCount() {
        return selectCount(null);
    }

    /**
     * 查询是否符合条件的数据
     *
     * @param record 查询条件
     * @return 匹配结果
     */
    default boolean exists(T record) {
        return selectCount(record) > 0;
    }

}
