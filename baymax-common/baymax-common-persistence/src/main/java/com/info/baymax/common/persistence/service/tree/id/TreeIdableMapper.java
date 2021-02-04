package com.info.baymax.common.persistence.service.tree.id;

import com.info.baymax.common.core.exception.ServiceException;

import java.io.Serializable;
import java.util.List;

/**
 * 有主键的树结构数据类型查询接口
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2019-05-29 15:57
 */
public interface TreeIdableMapper<ID extends Serializable, T extends TreeIdable<ID, T>> {

    /**
     * 根据根节点主键查询地根节点及后代节点
     *
     * @param rootId 根节点主键
     * @return 根节点及子孙节点
     */
    T selectTree(ID rootId);

    /**
     * 根据父节点ID查询子集列表
     *
     * @param parentId 父节点ID
     * @return 子集列表
     * @throws ServiceException
     */
    List<T> selectByParentId(ID parentId);
}
