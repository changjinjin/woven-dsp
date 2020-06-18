package com.info.baymax.data.elasticsearch.service;

import com.google.gson.JsonObject;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult.Hit;

import java.util.List;

/**
 * ES操作 抽象方法 基本包含所有基本操作
 */
public interface JestService {

    /**
     * 删除索引
     *
     * @param type ：当前删除document名称
     * @return
     */
    JestResult deleteIndex(String type);

    /**
     * 清除缓存
     *
     * @return
     */
    JestResult clearCache();

    /**
     * 关闭索引
     *
     * @param type ：文档表示的对象类别
     * @return
     */
    JestResult closeIndex(String type);

    /**
     * 优化索引
     *
     * @return
     */
    JestResult optimizeIndex();

    /**
     * 刷新索引
     *
     * @return
     */
    JestResult flushIndex();

    /**
     * 判断索引是否存在
     *
     * @return
     */
    JestResult indicesExists();

    /**
     * 查看节点信息
     *
     * @return
     */
    JestResult nodesInfo();

    /**
     * 查看集群健康信息
     *
     * @return
     */
    JestResult health();

    /**
     * 节点状态
     *
     * @return
     */
    JestResult nodesStats();

    /**
     * 更新Document
     *
     * @param index ：文档在哪存放
     * @param type  ： 文档表示的对象类别
     * @param id    ：文档唯一标识
     */
    JestResult updateDocument(String script, String index, String type, String id);

    /**
     * 删除Document
     *
     * @param index ：文档在哪存放
     * @param type  ： 文档表示的对象类别
     * @param id    ：文档唯一标识
     * @return
     */
    JestResult deleteDocument(String index, String type, String id);

    /**
     * 根据条件删除
     *
     * @param index
     * @param type
     * @param params
     */
    JestResult deleteDocumentByQuery(String index, String type, String params);

    /**
     * 获取Document
     *
     * @param o     ：返回对象
     * @param index ：文档在哪存放
     * @param type  ： 文档表示的对象类别
     * @param id    ：文档唯一标识
     * @return
     */
    <T> JestResult getDocument(Class<T> clazz, String index, String type, String id);

    // // Suggestion
    // List<SuggestResult.Suggestion> suggest();

    /**
     * 查询全部
     *
     * @param index ：文档在哪存放
     * @return
     */
    <T> List<Hit<T, Void>> searchAll(String index, Class<T> clazz);

    /**
     * 搜索
     *
     * @param keyWord ：搜索关键字
     * @return
     */
    <T> List<Hit<T, Void>> createSearch(String keyWord, String type, Class<T> clazz, String... fields);

    // bulkIndex操作
    <T> void bulkIndex(String index, String type, T o);

    /**
     * 创建索引
     *
     * @param o     ：返回对象
     * @param index ：文档在哪存放
     * @param type  ： 文档表示的对象类别
     * @return
     */
    <T> JestResult createIndex(T o, String index, String type);

    /**
     * 搜索事件流图表数据
     *
     * @param param
     * @return
     */
    JsonObject searchEvent(String param);

    /**
     * @param scrollId
     * @return
     */
    JsonObject searchEventHistogramByScroll(String scrollId);

    /**
     * @param param
     * @return
     */
    JsonObject searchInitEventHistogram(String param);

}
