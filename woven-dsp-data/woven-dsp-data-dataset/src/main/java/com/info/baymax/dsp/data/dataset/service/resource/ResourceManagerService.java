package com.info.baymax.dsp.data.dataset.service.resource;

import com.google.common.collect.Lists;
import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.jpa.page.Page;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import com.info.baymax.dsp.data.dataset.entity.security.ResourceDesc;
import com.info.baymax.dsp.data.dataset.entity.security.Tenant;
import com.info.baymax.dsp.data.dataset.service.core.DatasetService;
import com.info.baymax.dsp.data.dataset.service.core.FlowDescService;
import com.info.baymax.dsp.data.dataset.service.core.SchemaService;
import com.info.baymax.dsp.data.dataset.service.security.ResourceDescService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Component("resourceManagerService")
@EnableCaching
public class ResourceManagerService extends AbstractSharedResourceService {

    @Autowired
    private SchemaService schemaService;
    @Autowired
    private DatasetService datasetService;
    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private DataProjectRefService dataProjectRefService;
    @Autowired
    private FlowDescService flowDescService;
    @Autowired
    private StandardBundleAppService standardBundleAppService;
    @Autowired
    private StandardBundleService standardBundleService;
    @Autowired
    public ResourceDescService resourceDescService;
    @Autowired
    public FilesetService filesetService;

    static String ROOT_DATA_SOURCE = "Datasources";
    static String ROOT_DATA_META = "Standards";
    static String ROOT_DATA_SET = "Datasets";
    static String ROOT_DATA_SCHEMA = "Schemas";
    static String ROOT_DATA_FLOW = "Flows";
    static String ROOT_FILE_SET = "Filesets";

    static String DIR_SHARED = "Shared";

    static String TYPE_SCHEMA = "schema_dir";
    static String TYPE_DATASET = "dataset_dir";
    static String TYPE_DATASOURCE = "datasource_dir";
    static String TYPE_STANADARD = "standard_dir";
    static String TYPE_FLOW = "flow_dir";
    static String TYPE_FILESET = "fileset_dir";

    static String FLAG_SHARED_DIR = "::SHARED_DIR::";

    static String[] ROOTS = {ROOT_DATA_SOURCE, ROOT_DATA_META, ROOT_DATA_SET, ROOT_DATA_SCHEMA, ROOT_DATA_FLOW,
        ROOT_FILE_SET};

    static String[] TYPES = {TYPE_SCHEMA, TYPE_DATASET, TYPE_DATASOURCE, TYPE_STANADARD, TYPE_FLOW, TYPE_FILESET};

    /**
     * 初始化资源目录的根目录，就是把还没有创建的需要的根目录创建出来存入数据库中去
     */
    @Transactional(value = "transactionManager")
    public void initRootsDir() {

        // 查询已经存在的根节点
        List<ResourceDesc> roots = findRootsInner(false, null, null);
        List<String> rootNames = Lists.newArrayList();
        if (roots != null && roots.size() > 0) {
            rootNames = roots.stream().map(t -> t.getName()).collect(Collectors.toList());
        }

        // 遍历判断列表中的根节点是否已经创建，没有创建则创建入库
        for (String n : ROOTS) {
            // 存在则跳过
            if (rootNames.contains(n))
                continue;

            // 保存没有初始化的根节点到数据库中去
            ResourceDesc r = new ResourceDesc();
            r.setName(n);
            r.setResType(matchResTypeByDirName(n));
            r.setTenantId(SaasContext.getCurrentTenantId());
            User user = SaasContext.getCurrentUser();
            r.setOwner(user.getId());
            r.setCreator(user.getLoginId());
            r.setLastModifier(user.getLoginId());

            resourceDescService.saveOrUpdate(r);
        }
    }

    @Override
    ResourceDesc getRoot(String id, String tenant) {
        return null;
    }

    @Override
    List<ResourceDesc> querySharedResources() {
        QueryObject queryObject = QueryObject.builder().limit(10000)//
            .addField("tenantId", SaasContext.getCurrentTenantId())//
            .orderByDesc("lastModifiedTime");
        Page<ResourceDesc> page = resourceDescService.findObjectPage(queryObject);
        return page != null ? page.getContent() : null;
    }

    @Override
    public List<ResourceDesc> findRootsInner(boolean withChildren, String includes, String excludes) {
        return findRootsInner(withChildren, includes, excludes, null, null, SaasContext.getCurrentUser(), null,
            getTenant(null).getId());
    }

    @Override
    ResourceDesc buildRes(String user, List<ResourceDesc> children) {
        ResourceDesc res = new ResourceDesc();
        res.setResType("user");
        res.setName(user);
        res.setChildren(children);
        return res;
    }

    @Override
    public ResourceDesc findResourceInner(String id, String includes, String tenantId) {
        Tenant t = getTenant(tenantId);
        ResourceDesc r = resourceDescService.findOne(t.getId(), id);
        if (r != null) {
            List<ResourceDesc> children = findResourceByParent(Arrays.asList(r), 2, null, null,
                includes == null ? null : includes.split(";"), t.getId());
            r.setChildren(children);
        }
        return r;
    }

    private String matchResTypeByDirName(String dirName) {
        switch (dirName) {
            case "Schemas":
                return TYPE_SCHEMA;
            case "Datasets":
                return TYPE_DATASET;
            case "Standards":
                return TYPE_STANADARD;
            case "Datasources":
                return TYPE_DATASOURCE;
            case "Flows":
                return TYPE_FLOW;
            case "Filesets":
                return TYPE_FILESET;
        }
        return null;
    }

    /**
     * 移动目录及其目录下内容到指定目录
     *
     * @param tenantId
     * @param user
     * @param resource
     * @return
     */
    public ResourceDesc updateResource(String tenantId, User user, ResourceDesc resource) {
        resource.setLastModifiedTime(new Date());
        resource = this.updateResourceDesc(tenantId, user, resource);
        List<ResourceDesc> childrens = resourceDescService.findByParent(tenantId, resource.getId());
        if (resource.getChildren() == null) {
            resource.setChildren(new ArrayList<>());
        }
        resource.setChildren(childrens);
        return resource;
    }

    /**
     * 移动选中的列表行到指定目录
     *
     * @param ids
     * @param dirId
     */
    @Transactional(value = "transactionManager")
    public void moveRowstoDIr(String[] ids, String dirId) {
        ResourceDesc resource = resourceDescService.selectByPrimaryKey(dirId);
        String resType = resource.getResType();
        switch (resType) {
            case "schema_dir": {
                schemaService.updateResourceIdByInstanceIds(ids, dirId);
            }
            case "dataset_dir": {
                datasetService.updateResourceIdByInstanceIds(ids, dirId);
                break;
            }
            case "datasource_dir": {
                dataSourceService.updateResourceIdByInstanceIds(ids, dirId);
                break;
            }
            case "standard_dir": {
                standardBundleService.updateResourceIdByInstanceIds(ids, dirId);
                break;
            }
            case "flow_dir": {
                flowDescService.updateResourceIdByInstanceIds(ids, dirId);
                break;
            }
            case "fileset_dir": {
                filesetService.updateResourceIdByInstanceIds(ids, dirId);
                break;
            }
        }
    }

    /**
     * 创建Resource
     *
     * @param tenantId
     * @param user
     * @param resource
     * @return
     */
    public ResourceDesc createResource(String tenantId, User user, ResourceDesc resource) {
        ResourceDesc parent = findOneById(resource.getParentId());
        int maxOrder = findMaxOrderByParent(tenantId, parent);
        resource.setOrder(maxOrder + 1);
        resource.setCreator(user.getLoginId());
        if (resource.getTenantId() == null)
            resource.setTenantId(tenantId);
        String msg = verifyResourceName(resource);
        if (msg != null)
            throw new IllegalArgumentException(msg);

        resource = save(tenantId, user, resource, parent);

        return resource;
    }

    /**
     * 检查同级目录下是否已经有同名的目录存在
     *
     * @param tenantId
     * @param user
     * @param resource
     * @return
     */
    public boolean isNameDuplicatedInParentDir(String tenantId, User user, ResourceDesc resource) {
        ResourceDesc resourceDesc = resourceDescService.findByNameAndParent(tenantId, resource.getName(),
            resource.getParentId());
        return resourceDesc != null;
    }

    /**
     * 删除目录及其子目录，以及目录下的东西和对应的关系表数据
     *
     * @param tenantId
     * @param user
     * @param resourceId
     */
    @Transactional(value = "transactionManager")
    public void deleteResource(String tenantId, User user, String resourceId) {
        ResourceDesc resource = findOneById(resourceId);
        if (TYPE_SCHEMA.equals(resource.getResType())) {
            String[] ids = schemaService.selectInstanceIdsByResourceId(resourceId);
            int status = schemaService.deleteByResourceId(resourceId);
            if (status > 0) {
                dataProjectRefService.deleteByInstanceId(ids, INSTANCE_TYPE_SCHEMA);
            }
        } else if (TYPE_DATASET.equals(resource.getResType())) {
            String[] ids = datasetService.selectInstanceIdsByResourceId(resourceId);
            int status = datasetService.deleteByResourceId(resourceId);
            if (status > 0) {
                dataProjectRefService.deleteByInstanceId(ids, INSTANCE_TYPE_DATASET);
            }
        } else if (TYPE_DATASOURCE.equals(resource.getResType())) {
            dataSourceService.deleteByResourceId(resourceId);
        } else if (TYPE_STANADARD.equals(resource.getResType())) {
            String[] ids = dataSourceService.selectInstanceIdsByResourceId(resourceId);
            // remove StandardBundle MapDb Hdfs File
            if (ids != null && ids.length > 0) {
                standardBundleAppService.deleteByIds(ids);
                standardBundleService.deleteByResourceId(resourceId);
            }
        } else if (TYPE_FLOW.equals(resource.getResType())) {
            String[] ids = flowDescService.selectInstanceIdsByResourceId(resourceId);
            int status = flowDescService.deleteByResourceId(resourceId);
            if (status > 0) {
                dataProjectRefService.deleteByInstanceId(ids, "flow");
            }
        } else if (TYPE_FILESET.equals(resource.getResType())) {
        }
        List<ResourceDesc> childrens = resourceDescService.findAllByParent(tenantId, resource.getId());
        if (childrens != null && !childrens.isEmpty()) {
            for (int i = 0; i < childrens.size(); i++) {
                deleteResource(tenantId, user, childrens.get(i).getId());
            }
        }
        resourceDescService.delete(tenantId, user.getId(), resourceId);
    }

    /**
     * 检查目录下的资源(Schema,Dataset)是否被占用，如果被占用就不能进行删除操作
     *
     * @param tenant
     * @param resourceId 目录Id
     * @param offset
     * @param limit
     * @return 正在使用改目录下的Schema的Dataset列表
     */
    public Page<Dataset> checkSchemaUsed(Tenant tenant, String resourceId, int offset, int limit) {
        ResourceDesc resource = this.findOneById(resourceId);
        if (TYPE_SCHEMA.equals(resource.getResType())) {
            List<String> ids = schemaService.getInstanceIdsByResourcePath(QueryObject.builder().offset(0).limit(1000),
                getPathByResourceId(resourceId));
            if (ICollections.hasElements(ids)) {
                QueryObject queryObject = QueryObject.builder()//
                    .offset(offset).limit(limit)//
                    .addField("tenantId", tenant.getId(), JpaCriteriaHelper.ComparatorOperator.EQUAL)//
                    .addField("schemaId", ids, JpaCriteriaHelper.ComparatorOperator.IN);
                Page<Dataset> page = datasetService.findObjectPage(queryObject);
                if (ids.size() <= 1000) {
                    return page;
                } else {
                    return new Page<Dataset>(1, ids.size(), ids.size(), page.getContent());
                }
            }
        }
        return null;
    }

    public ResourceDesc findOneById(String id) {
        return resourceDescService.selectByPrimaryKey(id);
    }

    public List<ResourceDesc> findResourceByIds(String tenantId, String[] ids) {
        return resourceDescService.findByIds(tenantId, ids);
    }

    public int findMaxOrderByParent(String tenantId, ResourceDesc parent) {
        Integer maxOrder = resourceDescService.findMaxOrderByParent(tenantId, parent.getId());
        return maxOrder != null ? maxOrder : 0;
    }

    @Transactional
    public ResourceDesc updateResourceDesc(String tenantId, User user, ResourceDesc resource) {
        resource.setTenantId(tenantId);
        if (StringUtils.isEmpty(resource.getOwner()) || "SYSTEM".equals(resource.getOwner())) {
            resource.setOwner(user.getId());
        }
        resource.setCreator(user.getLoginId());
        resource.setLastModifier(user.getLoginId());
        if (resource.getParentId() != null && resource.getId() != null) {
            resource = updateResourceDesc(tenantId, resource);
            return resource;
        }
        if (resource.getId() != null) {
            resource = resourceDescService.saveOrUpdate(resource);
            return resource;
        }
        return null;
    }

    @Transactional
    public ResourceDesc save(String tenantId, User user, ResourceDesc resource, ResourceDesc parent) {
        resource.setTenantId(tenantId);
        if (StringUtils.isEmpty(resource.getOwner()) || "SYSTEM".equals(resource.getOwner())) {
            resource.setOwner(user.getId());
        }
        resource.setCreator(user.getLoginId());
        resource.setLastModifier(user.getLoginId());
        if (resource.getChildren() == null) {
            resource.setChildren(new ArrayList<>());
        }
        if (parent == null) {
            resource.setPath(resource.getName() + ";");
        } else {
            resource.setPath(parent.getPath() + resource.getName() + ";");
        }
        return resourceDescService.saveOrUpdate(resource);
    }

    private ResourceDesc updateResourceDesc(String tenantId, ResourceDesc resource) {
        ResourceDesc oldResource = resourceDescService.selectByPrimaryKey(resource.getId());
        String oldPath = "";
        oldPath = oldResource.getPath();
        String newPath = "";
        if (!StringUtils.isEmpty(oldPath)) {
            String parentPath = SimpleHelper.getPathRecursion(resource.getParentId(), resourceDescService,
                new StringBuilder(), true);
            newPath = parentPath.substring(0, parentPath.lastIndexOf(";")) + ";" + resource.getName() + ";";
        } else {
            String parentPath = SimpleHelper.getPathRecursion(resource.getParentId(), resourceDescService,
                new StringBuilder(), true);
            if (!StringUtils.isEmpty(parentPath)) {
                parentPath = parentPath.substring(0, parentPath.lastIndexOf(";"));
                newPath = parentPath.substring(0, parentPath.lastIndexOf(";")) + ";" + resource.getName() + ";";
            } else {
                newPath = resource.getName() + ";";
            }
        }
        newPath = newPath.replaceAll(";;", ";");
        resource.setPath(newPath);
        resource = resourceDescService.saveOrUpdate(resource);
        resourceDescService.updatePath(oldPath + "%", oldPath.length() + 1, newPath);
        return resource;
    }

    @Transactional(value = "transactionManager")
    public List<ResourceDesc> fsave(String tenantId, User user, List<ResourceDesc> resource) {
        List<ResourceDesc> saveResource = new ArrayList<>();
        for (int i = 0; i < resource.size(); i++) {
            ResourceDesc res = resource.get(i);
            res.setTenantId(tenantId);
            res.setOwner(user.getId());
            res.setCreator(user.getLoginId());
            res.setLastModifier(user.getLoginId());
            if (res.getChildren() == null) {
                res.setChildren(new ArrayList<>());
            }
            saveResource.add(res);
        }
        resourceDescService.insertListSelective(saveResource);
        return saveResource;
    }

    @Transactional(value = "transactionManager")
    public void saveResourcesWithOutTenantAndUser(List<ResourceDesc> resources) {
        if (ICollections.hasElements(resources)) {
            for (ResourceDesc t : resources) {
                resourceDescService.saveOrUpdate(t);
            }
        }
    }

    public ResourceDesc findOneByNameAndParent(String tenantId, String owner, String name, ResourceDesc parent) {
        return resourceDescService.findByNameAndParent(tenantId, name, parent.getId());
    }

    public String getPathByResourceId(String resourceId) {
        return SimpleHelper.getPathRecursion(resourceId, resourceDescService, new StringBuilder(), true);
    }

    // 根据条件查询全部的资源目录树，一次性从数据库查出所有属于用户的数据在内存中去处理层级关系避免迭代请求数据库资源
    public List<ResourceDesc> findResourceDescTree(String includes, String excludes, String strict, String names,
                                                   String allUser, String tenantId) {
        List<String> includeList = stringSplitToList(includes);
        List<String> excludeList = stringSplitToList(excludes);
        List<String> nameList = stringSplitToList(names);

        // 当前用户的资源目录树
        List<ResourceDesc> roots = findOneUserResourceTree(strict, tenantId, SaasContext.getCurrentUserId(),
            includeList, excludeList, nameList);
        // 查询别人的资源目录
        return populateResourceDescTreeForUsers(roots, allUser, strict, tenantId, includeList, excludeList, nameList);
    }

    public List<ResourceDesc> populateResourceDescTreeForUsers(List<ResourceDesc> roots, String allUser, String strict,
                                                               String tenantId, List<String> includeList, List<String> excludeList, List<String> nameList) {
        if (StringUtils.isNotEmpty(allUser) && Boolean.valueOf(allUser)) {
            // 获取租户下面所有的除了当前用户的用户列表
            User u = SaasContext.getCurrentUser();
            List<User> users = new ArrayList<>();
            if (u.admin()) {
                users.addAll(Lists.newArrayList(userService.findAllByTenantId(u.getTenantId())));
                users = users.stream().filter(t -> !t.getId().equals(u.getId())).collect(Collectors.toList());
            }

            // 构建其他用户的资源目录
            ResourceDesc otherUserDir = new ResourceDesc();
            otherUserDir.setResType("user_dir");
            otherUserDir.setName("OtherUser");
            otherUserDir.setChildren(new ArrayList<>());
            for (User user : users) {
                otherUserDir.getChildren().add(buildRes(user.getName(),
                    findOneUserResourceTree(strict, tenantId, user.getId(), includeList, excludeList, nameList)));
            }
            roots.add(otherUserDir);
        }
        return roots;
    }

    // 查找一个人的资源目录树
    private List<ResourceDesc> findOneUserResourceTree(String strict, String tenantId, String owner,
                                                       List<String> includeList, List<String> excludeList, List<String> nameList) {
        List<ResourceDesc> oneUserAllResourceList = resourceDescService.findByTenantAndOwner(tenantId, owner);
        return facdeTree(findOneUserResourceRoots(oneUserAllResourceList, strict, includeList, excludeList, nameList),
            findOneUserResourceNotRoots(oneUserAllResourceList));
    }

    // 从所有的数据中心取出根节点列表
    private List<ResourceDesc> findOneUserResourceRoots(List<ResourceDesc> oneUserAllResourceList, String strict,
                                                        List<String> includeList, List<String> excludeList, List<String> nameList) {
        if (oneUserAllResourceList != null && !oneUserAllResourceList.isEmpty()) {
            return oneUserAllResourceList//
                .stream()//
                .filter(t -> t.getParentId() == null)//
                .filter(t -> (nameList != null && nameList.contains(t.getName()))
                    || isValid(includeList, excludeList, t, strict))//
                .sorted()//
                .collect(Collectors.toList());
        }
        return oneUserAllResourceList;
    }

    // 从所有的数据中心取出非根节点列表
    private List<ResourceDesc> findOneUserResourceNotRoots(List<ResourceDesc> oneUserAllResourceList) {
        if (oneUserAllResourceList != null && oneUserAllResourceList.size() > 0) {
            return oneUserAllResourceList.stream().filter(t -> t.getParentId() != null).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    // 处理根节点和非根节点之间的关系
    private List<ResourceDesc> facdeTree(List<ResourceDesc> roots, List<ResourceDesc> notRoots) {
        if (notRoots == null || notRoots.isEmpty()) {
            return roots;
        }
        Iterator<ResourceDesc> iterator = null;
        for (ResourceDesc root : roots) {
            iterator = notRoots.iterator();
            // 遍历非根节点查找节点的子节点
            List<ResourceDesc> children = root.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                root.setChildren(children);
            }
            while (iterator.hasNext()) {
                ResourceDesc next = iterator.next();
                if (next.getParentId() != null && root.getId().equals(next.getParentId())) {
                    children.add(next);
                    // 已经有关系的节点从非根节点集中删除以减少迭代的次数
                    iterator.remove();
                }
            }
            // 子集非空，递归调用给子层查找子集，直到子集没有找到子集
            if (children != null && !children.isEmpty()) {
                facdeTree(children, notRoots);
            }
        }
        return roots;
    }

    public Tenant getTenant(String tenantId) {
        return simpleHelper.getOrElseTenant(tenantId);
    }
}
