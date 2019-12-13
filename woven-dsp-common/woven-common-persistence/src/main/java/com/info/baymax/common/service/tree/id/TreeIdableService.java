package com.info.baymax.common.service.tree.id;

import com.google.common.collect.Lists;
import com.info.baymax.common.utils.ICollections;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树结构数据查询公共接口，提取一些比较通用的方法，如：平铺的数据集转化成只有根节点的数据集
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2019年9月24日 下午5:38:52
 */
public interface TreeIdableService<ID extends Serializable, T extends TreeIdable<ID, T>> {

    /**
     * 通过平铺的数据集合转换成一个有树结构的数据集合，即所有根节点的集合，每个根节点包含自己所有的字段节点，只有一个根
     *
     * @param allList 平铺的数据集合
     * @return 根节点
     */
    default T fetchRoot(List<T> allList) {
        List<T> list = fetchTree(findRoots(allList), findNotRoots(allList));
        if (ICollections.hasElements(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 通过平铺的数据集合转换成一个有树结构的数据集合，即所有根节点的集合，每个根节点包含自己所有的字段节点
     *
     * @param allList 平铺的数据集合
     * @return 根节点的集合，每个根节点包含自己所有的字段节点
     */
    default List<T> fetchTree(List<T> allList) {
        return fetchTree(findRoots(allList), findNotRoots(allList));
    }

    /**
     * 从所有平铺的数据集合中心取出根节点列表
     *
     * @param allList 所有平铺的数据集合
     * @return 根节点集合
     */
    default List<T> findRoots(List<T> allList) {
        if (ICollections.hasElements(allList))
            return allList.stream().filter(t -> t.getParentId() == null).sorted().collect(Collectors.toList());
        return allList;
    }

    /**
     * 从所有平铺的数据集合中心取出非根节点列表
     *
     * @param allList 所有平铺的数据集合
     * @return 非根节点集合
     */
    default List<T> findNotRoots(List<T> allList) {
        if (ICollections.hasElements(allList))
            return allList.stream().filter(t -> t.getParentId() != null).collect(Collectors.toList());
        return Lists.newArrayList();
    }

    /**
     * 处理根节点和非根节点之间的关系
     *
     * @param roots    根节点集合
     * @param notRoots 非根节点集合
     * @return 最终转化成的根节点集合，根节点包含子孙集合数据
     */
    default List<T> fetchTree(List<T> roots, List<T> notRoots) {
        if (ICollections.hasElements(notRoots)) {
            Iterator<T> iterator = null;
            for (T root : roots) {
                iterator = notRoots.iterator();
                // 遍历非根节点查找节点的子节点
                List<T> children = root.getChildren();
                if (children == null) {
                    children = Lists.newArrayList();
                    root.setChildren(children);
                }
                while (iterator.hasNext()) {
                    T next = iterator.next();
                    if (next.getParentId() != null && root.getId().equals(next.getParentId())) {
                        children.add(next);
                        // 已经有关系的节点从非根节点集中删除以减少迭代的次数
                        iterator.remove();
                    }
                }
                // 子集非空，递归调用给子层查找子集，直到子集没有找到子集
                if (ICollections.hasElements(children)) {
                    fetchTree(children, notRoots);
                }
            }
        }
        return roots;
    }
}
