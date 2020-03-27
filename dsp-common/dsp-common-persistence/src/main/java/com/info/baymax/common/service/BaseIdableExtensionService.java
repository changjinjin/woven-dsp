package com.info.baymax.common.service;

import com.info.baymax.common.entity.id.Idable;

import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * 存在主键属性的实体类一些公共的接口定义
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-29 15:21
 */
@Transactional
public interface BaseIdableExtensionService<ID extends Serializable, T extends Idable<ID>>
    extends BaseExtensionService<T>, BaseIdableService<ID, T> {
}
