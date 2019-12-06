package com.merce.woven.common.base.service;

import com.merce.woven.common.entity.preprocess.annotation.PreInsert;
import com.merce.woven.common.entity.preprocess.annotation.PreUpdate;
import com.merce.woven.common.entity.preprocess.annotation.Preprocess;
import com.merce.woven.common.mybatis.mapper.MyBaseMapper;
import com.merce.woven.common.mybatis.mapper.MyIdableMapper;
import com.merce.woven.common.mybatis.mapper.base.BaseExampleMapper;
import com.merce.woven.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

/**
 * 存在主键属性的实体操作接口定义
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:22
 */
@Transactional
public interface BaseIdableService<T> extends BaseService<T>, MyIdableMapper<T> {

    MyIdableMapper<T> getMyIdableMapper();

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

    @Override
    default MyBaseMapper<T> getMyBaseMapper() {
        return getMyIdableMapper();
    }

    @Preprocess
    @Override
    default int insertListWithPrimaryKey(@PreInsert List<T> recordList) {
        preInsert(recordList);
        return getMyIdableMapper().insertListWithPrimaryKey(recordList);
    }

    @Preprocess
    @Override
    default int insertUseGeneratedKeys(@PreInsert T record) {
        preInsert(record);
        return getMyIdableMapper().insertUseGeneratedKeys(record);
    }

    @Preprocess
    @Override
    default int replace(@PreInsert T record) {
        preInsert(record);
        return getMyIdableMapper().replace(record);
    }

    @Preprocess
    @Override
    default int replaceListWithPrimaryKey(@PreInsert List<T> recordList) {
        preInsert(recordList);
        return getMyIdableMapper().replaceListWithPrimaryKey(recordList);
    }

    @Preprocess
    @Override
    default int updateByPrimaryKey(@PreUpdate T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKey(record);
    }

    @Preprocess
    @Override
    default int updateByPrimaryKeySelective(@PreUpdate T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKeySelective(record);
    }

    @Preprocess
    @Override
    default int updateListByPrimaryKey(@PreUpdate List<T> recordList) {
        preUpdate(recordList);
        return getMyIdableMapper().updateListByPrimaryKey(recordList);
    }

    @Preprocess
    @Override
    default int updateListByPrimaryKeySelective(@PreUpdate List<T> recordList) {
        preUpdate(recordList);
        return getMyIdableMapper().updateListByPrimaryKeySelective(recordList);
    }

    @Override
    default int deleteByPrimaryKey(Object key) {
        if (key == null) {
            throw new ParameterCheckException("根据主键删除数据时参数key不能为空");
        }
        return getMyIdableMapper().deleteByPrimaryKey(key);
    }

    @Override
    default int deleteListByPrimaryKey(List<T> recordList) {
        if (ICollections.hasNoElements(recordList)) {
            throw new ParameterCheckException("根据主键批量删除数据时参数recordList不能为空");
        }
        return getMyIdableMapper().deleteListByPrimaryKey(recordList);
    }

    @Override
    default int deleteByPrimaryKeys(List<?> keys) {
        if (ICollections.hasNoElements(keys)) {
            throw new ParameterCheckException("根据主键批量删除数据时参数keys不能为空");
        }
        return getMyIdableMapper().deleteByPrimaryKeys(keys);
    }

    /**
     * 根据主键值数组批量删除
     *
     * @param keys 主键值数组
     * @return 删除结果
     */
    default int deleteByPrimaryKeys(Object[] keys) {
        if (keys == null || keys.length < 1) {
            throw new ParameterCheckException("根据主键批量删除数据时参数keys不能为空");
        }
        return deleteByPrimaryKeys(Arrays.asList(keys));
    }

    @Override
    default T selectByPrimaryKey(Object key) {
        return getMyIdableMapper().selectByPrimaryKey(key);
    }

    @Override
    default List<T> selectByPrimaryKeys(List<?> keys) {
        return getMyIdableMapper().selectByPrimaryKeys(keys);
    }

    /**
     * 根据主键值数组查询
     *
     * @param keys 主键值数组
     * @return 数据结果集合
     */
    default List<T> selectByPrimaryKeys(Object... keys) {
        return selectByPrimaryKeys(Arrays.asList(keys));
    }

    @Override
    default boolean existsWithPrimaryKey(Object key) {
        return getMyIdableMapper().existsWithPrimaryKey(key);
    }

    @Override
    default int deleteByIds(String ids) {
        if (StringUtils.isEmpty(ids)) {
            throw new ParameterCheckException("根据主键批量删除数据时参数ids不能为空");
        }
        return getMyIdableMapper().deleteByIds(ids);
    }

    @Override
    default List<T> selectByIds(String ids) {
        return getMyIdableMapper().selectByIds(ids);
    }

}
