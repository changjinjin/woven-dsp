package com.info.baymax.dsp.data.dataset.service.resource;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.service.criteria.ExampleQueryService;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassService;
import com.info.baymax.common.utils.ICollections;

/**
 * 与资源目录相关的通用方法接口
 *
 * @param <T> 继承ResourceId的类型
 * @author jingwei.yang
 * @date 2019年10月17日 下午2:16:54
 */
public interface ResourceIdService<T extends ResourceId> extends EntityClassService<T>, ExampleQueryService<T> {

    default List<T> selectByResourceId(String resourceId) {
        return selectList(
            ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("resourceId", resourceId).end());
    }

    default String[] selectInstanceIdsByResourceId(String resourceId) {
        List<T> list = selectList(ExampleQuery.builder(getEntityClass()).selectProperties("id").fieldGroup()
            .andEqualTo("resourceId", resourceId).end());
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).toArray(String[]::new);
        }
        return null;
    }

    default String[] selectInstanceIdsByResourceIds(String[] resourceIds) {
        List<T> list = selectList(ExampleQuery.builder(getEntityClass()).selectProperties("id").fieldGroup()
            .andIn("resourceId", resourceIds).end());
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).toArray(String[]::new);
        }
        return null;
    }

    default int deleteByResourceId(String resourceId) {
        return delete(ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("resourceId", resourceId).end());
    }

    default int updateResourceIdByInstanceId(String instanceId, String resourceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceId", resourceId);
        return updateByExampleSelective((T) JSON.parseObject(jsonObject.toJSONString(), getEntityClass()),
            ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("id", instanceId).end());
    }

    default int updateResourceIdByInstanceIds(String[] instanceIds, String resourceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceId", resourceId);
        return updateByExampleSelective((T) JSON.parseObject(jsonObject.toJSONString(), getEntityClass()),
            ExampleQuery.builder(getEntityClass()).fieldGroup().andIn("id", instanceIds).end());
    }

    default int updateNewResourceIdByOldResourceId(String newResourceId, String oldResourceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("resourceId", newResourceId);
        return updateByExampleSelective((T) JSON.parseObject(jsonObject.toJSONString(), getEntityClass()),
            ExampleQuery.builder(getEntityClass()).fieldGroup().andEqualTo("resourceId", oldResourceId).end());
    }

}
