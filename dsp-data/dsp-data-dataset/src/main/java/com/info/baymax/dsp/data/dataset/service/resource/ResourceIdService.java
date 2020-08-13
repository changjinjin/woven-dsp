package com.info.baymax.dsp.data.dataset.service.resource;

import java.util.List;

import com.google.common.collect.ImmutableMap;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.criteria.example.ExampleQueryService;
import com.info.baymax.common.queryapi.field.FieldGroup;
import com.info.baymax.common.service.entity.EntityClassService;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.utils.JsonUtils;

/**
 * 与资源目录相关的通用方法接口
 *
 * @param <T> 继承ResourceId的类型
 * @author jingwei.yang
 * @date 2019年10月17日 下午2:16:54
 */
public interface ResourceIdService<T extends ResourceId> extends EntityClassService<T>, ExampleQueryService<T> {

    default List<T> selectByResourceId(String resourceId) {
        return selectList(ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andEqualTo("resourceId", resourceId)));
    }

    default String[] selectInstanceIdsByResourceId(String resourceId) {
        List<T> list = selectList(ExampleQuery.builder(getEntityClass()).selectProperties("id")
            .fieldGroup(FieldGroup.builder().andEqualTo("resourceId", resourceId)));
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).toArray(String[]::new);
        }
        return null;
    }

    default String[] selectInstanceIdsByResourceIds(String[] resourceIds) {
        List<T> list = selectList(ExampleQuery.builder(getEntityClass()).selectProperties("id")
            .fieldGroup(FieldGroup.builder().andIn("resourceId", resourceIds)));
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).toArray(String[]::new);
        }
        return null;
    }

    default int deleteByResourceId(String resourceId) {
        return delete(ExampleQuery.builder(getEntityClass())
            .fieldGroup(FieldGroup.builder().andEqualTo("resourceId", resourceId)));
    }

    default int updateResourceIdByInstanceId(String instanceId, String resourceId) {
        return updateByExampleSelective(
            JsonUtils.fromObject(ImmutableMap.<String, Object>of("resourceId", resourceId),
                getEntityClass()),
            ExampleQuery.builder(getEntityClass()).fieldGroup(FieldGroup.builder().andEqualTo("id", instanceId)));
    }

    default int updateResourceIdByInstanceIds(String[] instanceIds, String resourceId) {
        return updateByExampleSelective(
            JsonUtils.fromObject(ImmutableMap.<String, Object>of("resourceId", resourceId),
                getEntityClass()),
            ExampleQuery.builder(getEntityClass()).fieldGroup(FieldGroup.builder().andIn("id", instanceIds)));
    }

    default int updateNewResourceIdByOldResourceId(String newResourceId, String oldResourceId) {
        return updateByExampleSelective(
            JsonUtils.fromObject(ImmutableMap.<String, Object>of("resourceId", newResourceId),
                getEntityClass()),
            ExampleQuery.builder(getEntityClass())
                .fieldGroup(FieldGroup.builder().andEqualTo("resourceId", oldResourceId)));
    }

}
