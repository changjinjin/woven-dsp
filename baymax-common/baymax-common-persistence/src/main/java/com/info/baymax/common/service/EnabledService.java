package com.info.baymax.common.service;

import com.google.common.collect.ImmutableMap;
import com.info.baymax.common.entity.gene.Enabled;
import com.info.baymax.common.entity.gene.Idable;
import com.info.baymax.common.queryapi.exception.ServiceException;
import com.info.baymax.common.queryapi.result.ErrType;
import com.info.baymax.common.service.entity.EntityClassService;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 具有enabled属性的对象更新状态接口
 *
 * @param <ID> 主键类型
 * @param <E>  enabled 字段类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2020年11月12日 上午9:57:39
 */
public interface EnabledService<ID extends Serializable, E extends Serializable, T extends Enabled<E> & Idable<ID>>
    extends EntityClassService<T>, BaseIdableAndExampleQueryService<ID, T> {

    default int updateEnabled(ID id, E enabled) {
        if (id == null) {
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, "Entity id could not be null.");
        }
        return updateByPrimaryKeySelective(
            JsonUtils.fromObject(ImmutableMap.of("id", id, "enabled", enabled), getEntityClass()));
    }

    default int updateEnabled(List<ID> ids, E enabled) {
        if (ICollections.hasNoElements(ids)) {
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, "Entity ids could not be null or empty.");
        }
        return updateListByPrimaryKeySelective(ids.stream()
            .map(id -> JsonUtils.fromObject(ImmutableMap.of("id", id, "enabled", enabled), getEntityClass()))
            .collect(Collectors.toList()));
    }
}
