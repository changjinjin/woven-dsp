package com.merce.woven.common.base.tree;

import com.merce.woven.common.base.tree.id.TreeIdable;
import com.merce.woven.common.base.tree.id.TreeIdableMapper;
import com.merce.woven.common.base.tree.sreach.TreeSearchable;
import com.merce.woven.common.base.tree.sreach.TreeSearchableService;
import com.merce.woven.common.message.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

/**
 * 树状数据结构的数据模型业务处理接口定义
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:56
 */
public interface TreeableAndIdableService<ID extends Serializable, T extends TreeSearchable<T> & TreeIdable<ID, T>>
    extends TreeSearchableService<T> {

    TreeIdableMapper<ID, T> getTreeableMapper();

    /**
     * 树数据查询，可根据根节点主键和关键字检索
     *
     * @param rootId  根节点主键
     * @param keyword 检索关键字
     * @return 根节点及符合条件的后代节点
     * @throws ServiceException
     */
    default T selectTree(ID rootId, String keyword) throws ServiceException {
        return search(selectTree(rootId), keyword);
    }

    /**
     * 根据节点ID查询以此节点为根的所有子节点
     *
     * @param rootId 根节点ID
     * @return 该节点及所有的子节点树
     * @throws ServiceException
     */
    default T selectTree(ID rootId) throws ServiceException {
        return getTreeableMapper().selectTree(rootId);
    }

    /**
     * 根据父节点ID查询子集列表
     *
     * @param parentId 父节点ID
     * @return 子集列表
     */
    default List<T> selectByParentId(ID parentId) throws ServiceException {
        return getTreeableMapper().selectByParentId(parentId);
    }

}
