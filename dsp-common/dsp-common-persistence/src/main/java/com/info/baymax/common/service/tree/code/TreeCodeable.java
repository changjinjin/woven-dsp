package com.info.baymax.common.service.tree.code;

import com.info.baymax.common.service.tree.Treeable;

import java.io.Serializable;

/**
 * 根据编码(code)维护的树结构数据类型接口
 *
 * @param <C> 编码类型，即关系维护的字段类型
 * @param <T> 实体类型
 * @author jingwei.yang
 * @date 2019年9月24日 下午5:20:24
 */
public interface TreeCodeable<C extends Serializable, T extends TreeCodeable<C, T>> extends Treeable<T> {

    /**
     * 获取节点编码
     *
     * @return 节点编码
     */
    C getCode();

    /**
     * 设置节点编码
     *
     * @param code 节点编码
     */
    void setCode(C code);

    /**
     * 获取父节点编码
     *
     * @return 父节点编码
     */
    C getParentCode();

    /**
     * 设置父节点编码
     *
     * @param parentCode 父节点编码
     */
    void setParentCode(C parentCode);
}
