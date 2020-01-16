package com.info.baymax.dsp.data.dataset.service.security.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.security.ResourceDescMapper;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceDescServiceImpl extends EntityClassServiceImpl<ResourceDesc> implements ResourceDescService {

    @Autowired
    private ResourceDescMapper resourceDescMapper;

    private final String[] queryFields = {"id", "parentId", "resType", "name", "order", "path", "tenantId"};

    @Override
    public MyIdableMapper<ResourceDesc> getMyIdableMapper() {
        return resourceDescMapper;
    }

    @Override
    public List<ResourceDesc> findByTenantAndOwner(String tenantId, String owner) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_OWNER, owner)//
            .andEqualTo("isHide", 0)//
            .end());
    }

    @Override
    public List<ResourceDesc> findByParent(String tenantId, String parentId) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo("parentId", parentId)//
            .andEqualTo("isHide", 0)//
            .end());
    }

    @Override
    public List<ResourceDesc> findAllByParent(String tenantId, String parentId) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo("parentId", parentId)//
            .end());
    }

    @Override
    public ResourceDesc findByNameAndParent(String tenantId, String name, String parentId) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_NAME, name)//
            .andEqualTo("parentId", parentId)//
            .end());
    }

    @Override
    public List<ResourceDesc> findRoots(String tenantId) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andIsNull("parentId")//
            .end());
    }

    @Override
    public List<ResourceDesc> findByIds(String tenantId, String[] ids) {
        return selectList(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andIn("id", ids)//
            .end());
    }

    @Override
    public ResourceDesc findByNameAndResTypeAndParentIsNullAndTenantId(String name, String resType, String tenantId) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo("resType", resType)//
            .andEqualTo(PROPERTY_NAME, name)//
            .andIsNull("parentId")//
            .end());
    }

    @Override
    public Integer findMaxOrderByParent(String tenantId, String parentId) {
        return resourceDescMapper.findMaxOrderByParent(tenantId, parentId);
    }

    @Override
    public ResourceDesc findRootsByName(String tenantId, String name) {
        return selectOne(ExampleQuery.builder(getEntityClass())//
            .fieldGroup()//
            .andEqualTo(PROPERTY_TENANTID, tenantId)//
            .andEqualTo(PROPERTY_NAME, name)//
            .andIsNull("parentId")//
            .end());
    }

    @Override
    public void updatePath(String oldPath, int length, String newPath) {
        resourceDescMapper.updatePath(oldPath, length, newPath);
    }

    /**
     * 查询用户拥有的所有目录ID集合
     *
     * @param userId  用户ID
     * @param resType 资源类型
     * @return 用户拥有的所有目录ID集合
     */
    @Override
    public String[] selectUserResourceIdsByResTypeAndPath(String userId, String resType, String path) {
        List<ResourceDesc> resourceList = null;
        ExampleQuery query = ExampleQuery.builder(getEntityClass()).selectProperties("id").fieldGroup()
                .andLeftLike("path", path).end();

        resourceList = selectList(query.fieldGroup().andLeftLike("path", path).end());

        if (ICollections.hasElements(resourceList)) {
            return resourceList.stream().map(t -> t.getId()).toArray(String[]::new);
        }
        return null;
    }
}
