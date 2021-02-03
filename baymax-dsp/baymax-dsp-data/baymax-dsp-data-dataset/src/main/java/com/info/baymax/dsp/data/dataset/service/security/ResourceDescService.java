package com.info.baymax.dsp.data.dataset.service.security;

import com.info.baymax.common.persistence.entity.base.BaseMaintableService;
import com.info.baymax.common.persistence.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.common.persistence.service.tree.id.TreeIdableService;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;

import java.util.List;

public interface ResourceDescService extends BaseMaintableService<ResourceDesc>,
        QueryObjectCriteriaService<ResourceDesc>, TreeIdableService<String, ResourceDesc> {

    List<ResourceDesc> findByTenantAndOwner(String tenantId, String owner);

    List<ResourceDesc> findByParent(String tenantId, String parentId);

    List<ResourceDesc> findAllByParent(String tenantId, String parentId);

    ResourceDesc findByNameAndParent(String tenantId, String name, String parentId);

    List<ResourceDesc> findRoots(String tenantId);

    List<ResourceDesc> findByIds(String tenantId, String[] ids);

    ResourceDesc findByNameAndResTypeAndParentIsNullAndTenantId(String name, String resType, String tenantId);

    Integer findMaxOrderByParent(String tenantId, String parentId);

    ResourceDesc findRootsByName(String tenantId, String name);

    void updatePath(String oldPath, int length, String newPath);

    /**
     * 查询用户拥有的所有目录ID集合
     *
     * @param userId  用户ID
     * @param resType 资源类型
     * @param path    资源目录路径
     * @return 用户拥有的所有目录ID集合
     */
    String[] selectUserResourceIdsByResTypeAndPath(String userId, String resType, String path);
}
