package com.info.baymax.common.entity.id;

import java.io.Serializable;

/**
 * ID定义接口，一般声明该实体类有主键属性才需要继承该接口，无强制作用，只是便于识别该实体是否含有主键
 *
 * @param <PK>
 * @author jingwei.yang
 * @date 2019-05-29 15:59
 */
public interface Idable<PK extends Serializable> extends Serializable {

    /**
     * 获取主键信息
     *
     * @return 主键信息
     */
    PK getId();

    /**
     * 设置主键信息
     *
     * @param id 主键信息
     */
    void setId(PK id);

    /**
     * 判断ID是否为空
     *
     * @return ID是否为空
     */
    default boolean idIsNull() {
        return getId() == null;
    }

    /**
     * 判断ID是否不为空
     *
     * @return ID是否不为空
     */
    default boolean idIsNotNull() {
        return !idIsNull();
    }
}
