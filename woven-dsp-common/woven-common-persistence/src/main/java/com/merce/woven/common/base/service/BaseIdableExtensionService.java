package com.merce.woven.common.base.service;

import javax.transaction.Transactional;


/**
 * 存在主键属性的实体类一些公共的接口定义
 * @author jingwei.yang
 * @date 2019-05-29 15:21
 * @param <T>
 */
@Transactional
public interface BaseIdableExtensionService<T> extends BaseExtensionService<T>, BaseIdableService<T> {
}
