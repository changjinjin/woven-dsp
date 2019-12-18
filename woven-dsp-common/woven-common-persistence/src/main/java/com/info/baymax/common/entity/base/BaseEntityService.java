package com.info.baymax.common.entity.base;

import java.util.List;

import com.info.baymax.common.entity.preprocess.PreEntityService;
import com.info.baymax.common.mybatis.mapper.base.BaseExampleMapper;
import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;

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
        insertSelective(t);
        return t;
    }

    default T update(T t) {
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
			// 避免前端传过来空字符串，这里需要单独做处理以避免tk.mybatis.mapper的主键策略不生效
            t.setId(null);
            insertSelective(t);
        } else {
            updateByPrimaryKeySelective(t);
        }
        return t;
    }

    default T findOne(Long tenantId, String id) {
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
