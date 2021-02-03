package com.info.baymax.common.persistence.service.tree.id;

import com.info.baymax.common.persistence.service.tree.code.TreeCodeableService;

import java.io.Serializable;

/**
 * 树结构数据查询公共接口，提取一些比较通用的方法，如：平铺的数据集转化成只有根节点的数据集
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2019年9月24日 下午5:38:52
 */
public interface TreeIdableService<ID extends Serializable, T extends TreeIdable<ID, T>>
    extends TreeCodeableService<ID, T> {
}
