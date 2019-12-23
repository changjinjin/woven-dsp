package com.info.baymax.common.entity.base;

import com.info.baymax.common.entity.preprocess.PreEntityService;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;

import java.util.List;

/**
 * 继承{@link BaseEntity.java}的实体类公共查询接口定义
 *
 * @param <T> 继承BaseEntity的实体类型
 * @author jingwei.yang
 * @date 2019年7月2日 上午10:20:47
 */
public interface BaseEntityService<T extends BaseEntity>
    extends BaseIdableAndExampleQueryService<T>, PreEntityService<T> {

    public static final String PROPERTY_ID = "id";
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_OWNER = "owner";
    public static final String PROPERTY_TENANTID = "tenantId";
    public static final String PROPERTY_EXPIREDTIME = "expiredTime";

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

    @Override
    default void preInsert(T t) {
        t.preInsert();
    }

    @Override
    default void preUpdate(T t) {
        t.preUpdate();
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
     * @param t 操作数据
     * @return 结果
     */
    default T saveOrUpdate(T t) {
        if (t.getId() == null) {
            t.setId(null);
            return save(t);
        } else {
            return update(t);
        }
    }

    /**
     * 根据ID删除单条记录，如果需要做级联删除需重写该方法
     *
     * @param id 删除的ID
     * @return 删除结果
     */
    default int deleteById(Long id) {
        return this.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID批量删除，调用deleteById循环删除，适用于有附加操作的场景
     *
     * @param ids 删除的ID集合
     * @return 删除结果
     */
    default int deleteByIds(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                deleteById(id);
            }
            return ids.length;
        }
        return 0;
    }

    default T findOne(Long tenantId, Long id) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_ID, id)//
            .end());
    }

    default List<T> findByIds(Long tenantId, Object[] ids) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andIn(PROPERTY_ID, ids)//
            .end());
    }

    default T findOne(Long tenantId) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .end());
    }

    default List<T> findAllByTenantId(Long tenantId) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .offset(0, Integer.MAX_VALUE)//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .end());
    }

    default List<T> findExpired(Long expireTime) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
            .end());
    }

    default List<T> findAllByTenantIdAndName(Long tenantId, String name) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_NAME, name)//
            .end());
    }

    default List<T> findAllByTenantIdAndOwner(Long tenantId, String owner) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .end());
    }

    default int deleteExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andLessThan(PROPERTY_EXPIREDTIME, expireTime)//
            .end());
    }

    default int delete(Long tenantId, Long id) {
        return delete(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_ID, id)//
            .end());
    }

    default int delete(Long tenantId, Long owner, Long id) {
        return delete(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .andEqualTo(PROPERTY_ID, id)//
            .end());
    }

    default int deleteByIds(Long tenantId, Object[] ids) {
        return delete(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andIn(PROPERTY_ID, ids)//
            .end());
    }

    default int deleteByIds(Long tenantId, Long owner, Object[] ids) {
        return delete(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .andIn(PROPERTY_ID, ids)//
            .end());
    }

    default List<T> findAll(Long tenantId, Object[] ids) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andIn(PROPERTY_ID, ids)//
            .end());
    }

    default int count(Long tenantId) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .end());
    }

    default T findOneByName(Long tenantId, Long owner, Long name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .andEqualTo(PROPERTY_NAME, name)//
            .end());
    }

    default int deleteByOwner(Long owner) {
        return delete(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .end());
    }

    default boolean existsByTenantIdAndName(Long tenantId, String name) {
        Integer count = selectCount(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_NAME, name)//
            .end());
        return count > 0;
    }
}
