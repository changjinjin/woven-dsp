package com.info.baymax.common.service.tree.id;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.info.baymax.common.service.BaseIdableAndExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;

/**
 * 树结构数据查询公共接口，提取一些比较通用的方法，如：平铺的数据集转化成只有根节点的数据集
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2019年9月24日 下午5:38:52
 */
public interface TreeIdableExampleQueryService<ID extends Serializable, T extends TreeIdable<ID, T>>
    extends TreeIdableService<ID, T>, TreeIdableMapper<ID, T>, BaseIdableAndExampleQueryService<ID, T> {

    @Override
    default T selectTree(ID rootId) {
        T t = selectByPrimaryKey(rootId);
        if (t != null) {
            fetchAllChildren(t);
        }
        return t;
    }

    default List<T> selectChilrenTree(ID rootId) {
        return selectChilrenTree(rootId, new HashMap<String, Object>());
    }

    default List<T> selectChilrenTree(ID rootId, Map<String, Object> extParams) {
        List<T> roots = selectByParentId(rootId, extParams);
        for (T t : roots) {
            fetchAllChildren(t);
        }
        return roots;
    }

    default void fetchAllChildren(T t) {
        if (t == null) {
            return;
        }
        List<T> children = selectByParentId(t.getId());
        if (ICollections.hasElements(children)) {
            for (T t2 : children) {
                fetchAllChildren(t2);
            }
        }
        t.setChildren(children);
    }

    @Override
    default List<T> selectByParentId(ID parentId) {
        return selectByParentId(parentId, new HashMap<String, Object>());
    }

    default List<T> selectByParentId(ID parentId, Map<String, Object> extParams) {
        return selectList(ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andEqualTo("parentId", parentId).andAllEqualTo(extParams)));
    }

}
