package com.info.baymax.dsp.data.dataset.service.resource;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.info.baymax.common.jpa.criteria.QueryObjectCriteriaService;
import com.info.baymax.common.jpa.criteria.query.QueryObject;
import com.info.baymax.common.service.criteria.example.ExampleQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.jpa.page.Page;

/**
 * 根据目录获取对应的列表数据方法定义，用于特殊的根据resource目录或者project目录查询对应目录下面的数据列表
 *
 * @param <T> 查询的实体类型
 * @author jingwei.yang
 * @date 2019年5月7日 上午10:08:27
 */
public interface QueryObjectByResourceOrProjectService<T extends ResourceId> extends QueryObjectCriteriaService<T> {

    /**
     * 根据左侧目录id获取右侧实例id
     *
     * @param userId      用户ID
     * @param queryObject 分页数据
     * @throws IllegalAccessException
     * @throws Exception
     */
    default Page<T> getInstanceCollectionByResDirAndCondition(String userId, QueryObject queryObject) {
        ExampleQuery query = getInstanceCollectionByResDirAndConditionDynamicTable(userId, queryObject);
        if (query == null) {
            return new Page<T>(queryObject.getPageNum(), queryObject.getLimit(), 0, null);
        }
        return findObjectPage(query);
    }

    /**
     * 根据条件组装子查询的sql并包装成ExampleQuery对象
     *
     * @param userId      用户ID
     * @param queryObject 查询条件
     * @return 组合了子查询的语句的查询条件
     */
    ExampleQuery getInstanceCollectionByResDirAndConditionDynamicTable(String userId, QueryObject queryObject);

    /**
     * 根据path获取数据列表
     *
     * @param queryObject 其他过滤条件
     * @param path        资源目录的路径
     * @return 数据列表
     */
    default List<T> getInstancesByResourcePath(QueryObject queryObject, String path) {
        return selectList(getInstancesByResourcePathDynamicTable(QueryObject.builder(queryObject), path));
    }

    /**
     * 根据资源目录的路径获取查询的条件
     *
     * @param queryObject 其他过滤条件
     * @param path        资源目录的路径
     * @return 动态查询的条件
     */
    ExampleQuery getInstancesByResourcePathDynamicTable(QueryObject queryObject, String path);

    /**
     * 根据path获取数据ID列表
     *
     * @param queryObject 其他过滤条件
     * @param path        资源目录的路径
     * @return 数据列表
     */
    default List<String> getInstanceIdsByResourcePath(QueryObject queryObject, String path) {
        List<T> list = selectList(getInstanceIdsByResourcePathDynamicTable(QueryObject.builder(queryObject), path));
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    /**
     * 根据资源目录的路径获取查询的条件
     *
     * @param queryObject 其他过滤条件
     * @param path        资源目录的路径
     * @return 动态查询的条件
     */
    ExampleQuery getInstanceIdsByResourcePathDynamicTable(QueryObject queryObject, String path);

    /**
     * 根据项目目录分页查询数据列表
     *
     * @param queryObject 查询参数
     * @return 分页数据
     * @throws Exception
     */
    default Page<T> getInstanceCollectionByProjectDirAndCondition(QueryObject queryObject) {
        return findObjectPage(getInstanceCollectionByProjectDirAndConditionDynamicTable(queryObject));
    }

    /**
     * 根据条件组装子查询的sql并包装成ExampleQuery对象
     *
     * @param queryObject 查询条件
     * @return 组合了子查询的语句的查询条件
     */
    ExampleQuery getInstanceCollectionByProjectDirAndConditionDynamicTable(QueryObject queryObject);
}
