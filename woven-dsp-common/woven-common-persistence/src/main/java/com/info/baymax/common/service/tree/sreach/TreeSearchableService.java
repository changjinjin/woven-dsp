package com.info.baymax.common.service.tree.sreach;

import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 可数据搜索的树结构数据类型业务接口定义
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:58
 * @param <T>
 */
public interface TreeSearchableService<T extends TreeSearchable<T>> {

    /**
     * 树数据检索，可根据根节点和关键字检索符合的树数据
     *
     * @param root    根节点
     * @param keyword 检索关键字
     * @return 根节点及符合条件的后代节点
     * @throws ServiceException
     */
    default T search(T root, String keyword) throws ServiceException {
        // 如果根节点存在并且需要根据关键字检索结果
        if (root != null && StringUtils.isNotEmpty(keyword)) {
            List<T> searchList = search(root.getChildren(), keyword);
            if (ICollections.hasElements(searchList) || isMatch(root, keyword)) {
                root.setChildren(searchList);
            } else {
                root = null;
            }
        }
        return root;
    }

    /**
     * 根据关键字在树结构数据中查询需要的数据，最后保留数据的树状结构，根节点不变，将不符合的叶子都删除，最后符合的节点都变成叶子节点
     *
     * @param regex 检索字段
     * @return 检索筛选结果
     */
    default List<T> search(List<T> rootList, String regex) {
        List<T> Children = null;
        T node = null;
        Iterator<T> ite = rootList.iterator();
        while (ite.hasNext()) {
            node = ite.next();
            Children = node.getChildren();
            if (isMatch(node, regex) || ICollections.hasElements(Children)) {
                if (ICollections.hasElements(Children)) {
                    // 回调继续检索下一层数据
                    Children = search(Children, regex);
                    // 非叶子节点，更新到属性中
                    if (ICollections.hasElements(Children)) {
                        node.setChildren(Children);
                    }
                    // 叶子节点如果匹配数据则设置叶子属性，否则删除该节点
                    else {
                        if (isMatch(node, regex)) {
                            node.setLeaf(true);
                        } else {
                            ite.remove();
                        }
                    }
                } else {
                    node.setLeaf(true);
                }
            } else {
                ite.remove();
            }
        }
        // 检查是否完成了检索，如果没有继续回调该方法进行检索直到所有的检索完成
        if (isEnd(rootList, true)) {
            return rootList;
        } else {
            return search(rootList, regex);
        }
    }

    /**
     * 是否遍历结束
     *
     * @param b 初始值
     * @return 遍历结束
     */
    default boolean isEnd(List<T> Children, boolean b) {
        List<T> subs = null;
        if (ICollections.hasElements(Children)) {
            for (T node : Children) {
                subs = node.getChildren();
                if (ICollections.hasElements(subs)) {
                    b = isEnd(subs, b);
                } else {
                    b = b && node.isLeaf();
                }
            }
        }
        return b;
    }

    /**
     * 检查对象是否否匹配，这里是默认实现，如果不能满足自己的需求可以在子类中重写该方法
     *
     * @param regex 检索字段
     * @return 匹配
     */
    default boolean isMatch(T t, String regex) {
        boolean b = true;
        Pattern p = Pattern.compile(regex);
        String[] matchFeilds = t.matchFeilds();
        if (matchFeilds != null && matchFeilds.length > 0) {
            for (String s : matchFeilds) {
                b &= p.matcher(s).find();
            }
        }
        return b;
    }

}
