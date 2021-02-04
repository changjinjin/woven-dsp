package com.info.baymax.common.persistence.service;

import com.info.baymax.common.core.exception.ServiceException;
import com.info.baymax.common.core.result.ErrType;
import com.info.baymax.common.persistence.entity.gene.Idable;
import com.info.baymax.common.persistence.mybatis.mapper.MyBaseMapper;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 存在主键属性的实体操作接口定义
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:22
 */
@Transactional
public interface BaseIdableService<ID extends Serializable, T extends Idable<ID>>
    extends BaseService<T>, MyIdableMapper<T> {

    MyIdableMapper<T> getMyIdableMapper();

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

    @Override
    default MyBaseMapper<T> getMyBaseMapper() {
        return getMyIdableMapper();
    }

    default T save(T t) {
        if (t == null) {
            throw new ServiceException(ErrType.ENTITY_EMPTY, "保存对象不能为空");
        }
        insertSelective(t);
        return t;
    }

    default T update(T t) {
        if (t == null) {
            throw new ServiceException(ErrType.ENTITY_EMPTY, "修改对象不能为空");
        }
        updateByPrimaryKeySelective(t);
        return t;
    }

    /**
     * 插入或者更新：当ID值为空时插入新的数据，否则更新记录
     *
     * @param t           保存的数据
     * @param checkRecord 是否检查数据库中记录是否存在，需要查数据库
     * @return 结果
     */
    default T saveOrUpdate(T t, boolean checkRecord) {
        if (t.getId() == null || t.getId().toString().trim().equals("")) {
            t.setId(null);
            return save(t);
        } else {
            if (checkRecord && !existsWithPrimaryKey(t.getId())) {
                return save(t);
            }
            return update(t);
        }
    }

    /**
     * 插入或者更新：当ID值为空时插入新的数据，否则更新记录
     *
     * @param t 操作数据
     * @return 结果
     */
    default T saveOrUpdate(T t) {
        return saveOrUpdate(t, true);
    }

    /**
     * 根据ID删除单条记录，如果需要做级联删除需重写该方法
     *
     * @param id 删除的ID
     * @return 删除结果
     */
    default int deleteById(ID id) {
        return this.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID批量删除，调用deleteById循环删除，适用于有附加操作的场景
     *
     * @param ids 删除的ID集合
     * @return 删除结果
     */
    default int deleteByIds(List<ID> ids) {
        if (ICollections.hasElements(ids)) {
            for (ID id : ids) {
                deleteById(id);
            }
            return ids.size();
        }
        return 0;
    }

    @Override
    default int insertListWithPrimaryKey(List<T> recordList) {
        preInsert(recordList);
        return getMyIdableMapper().insertListWithPrimaryKey(recordList);
    }

    @Override
    default int insertUseGeneratedKeys(T record) {
        preInsert(record);
        return getMyIdableMapper().insertUseGeneratedKeys(record);
    }

    @Override
    default int replace(T record) {
        preInsert(record);
        return getMyIdableMapper().replace(record);
    }

    @Override
    default int replaceListWithPrimaryKey(List<T> recordList) {
        preInsert(recordList);
        return getMyIdableMapper().replaceListWithPrimaryKey(recordList);
    }

    @Override
    default int updateByPrimaryKey(T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKey(record);
    }

    @Override
    default int updateByPrimaryKeySelective(T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    default int updateListByPrimaryKey(List<T> recordList) {
        preUpdate(recordList);
        return getMyIdableMapper().updateListByPrimaryKey(recordList);
    }

    @Override
    default int updateListByPrimaryKeySelective(List<T> recordList) {
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
