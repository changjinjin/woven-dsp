package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.info.baymax.common.message.exception.ServiceException;
import com.info.baymax.common.message.result.ErrType;
import com.info.baymax.common.page.IPage;
import com.info.baymax.common.page.IPageable;
import com.info.baymax.common.service.criteria.agg.AggField;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.agg.AggType;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.config.jest.ISearchResult;
import com.info.baymax.dsp.access.dataapi.config.jest.JestClientUtils;
import com.info.baymax.dsp.access.dataapi.config.jest.JestConf;
import com.info.baymax.dsp.access.dataapi.data.*;
import com.inforefiner.repackaged.org.apache.curator.shaded.com.google.common.collect.Lists;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Component
public class ElasticSearchDataReader extends MapEntityDataReader {

    public ElasticSearchDataReader() {
        super(Engine.ELASTICSEARCH);
    }

    @Override
    public boolean supports(StorageConf conf) {
        return (conf instanceof ElasticSearchStorageConf) && super.supports(conf);
    }

    @Override
    public IPage<MapEntity> readRecord(StorageConf conf, RecordQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(), QueryParser.getInstance(ElasticsearchQueryDslParser.class)
            .parseRecordQuery((ElasticSearchStorageConf) conf, query), null, null);
    }

    @Override
    public IPage<MapEntity> readAgg(StorageConf conf, AggQuery query) throws Exception {
        return executeQuery(conf, query.getPageable(), QueryParser.getInstance(ElasticsearchQueryDslParser.class)
            .parseAggQuery((ElasticSearchStorageConf) conf, query), query.getAggFields(), query.getGroupFields());

    }

    private IPage<MapEntity> executeQuery(StorageConf conf, IPageable pageable, Search sreach,
                                          LinkedHashSet<AggField> aggFields, LinkedHashSet<String> groupFields) throws Exception {
        JestClient jestClient = null;
        try {
            JestConf jestConf = JestConf.from((ElasticSearchStorageConf) conf);
            jestClient = JestClientUtils.jestClient(jestConf);
            SearchResult result = new ISearchResult(jestClient.execute(sreach));
            if (result.isSucceeded()) {
                if (ICollections.hasNoElements(groupFields)) {
                    return IPage.<MapEntity>of(pageable, result.getTotal(),
                        result.getSourceAsObjectList(MapEntity.class, false));
                } else {
                    return IPage.<MapEntity>of(pageable, result.getTotal(),
                        parseAggResult(result, Lists.newArrayList(aggFields), Lists.newArrayList(groupFields)));
                }
            } else {
                log.error("es query failed,errorMessage:" + result.getErrorMessage());
            }
        } finally {
            if (jestClient != null) {
                try {
                    jestClient.close();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, e);
                }
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    private List<MapEntity> parseAggResult(SearchResult result, List<AggField> aggFields, List<String> groupFields) {
        JSONObject json = JSON.parseObject(result.getJsonString(), JSONObject.class);
        JSONObject aggregations = json.getJSONObject("aggregations");
        return parse(aggregations, aggFields, groupFields, Lists.newArrayList(), new MapEntity());
    }

    private List<MapEntity> parse(JSONObject json, List<AggField> aggFields, List<String> groupFields,
                                  List<MapEntity> result, MapEntity map) {
        JSONObject m = null;
        String group = null;

        for (String groupField : groupFields) {
            m = json.getJSONObject(groupField);
            if (m != null) {
                group = groupField;
                break;
            }
        }

        if (m != null) {
            JSONArray buckets = m.getJSONArray("buckets");
            if (ICollections.hasElements(buckets)) {
                for (Object object : buckets) {
                    JSONObject item = (JSONObject) object;
                    map.put(group, item.getString("key"));
                    parse(item, aggFields, groupFields, result, map);
                }
            }
        } else {
            MapEntity newMap = new MapEntity();
            newMap.putAll(map);
            for (AggField aggField : aggFields) {
                newMap.put(aggField.getAlias(), getvalue(json, aggField.getAggType(), aggField.getAlias()));
            }
            result.add(newMap);
        }
        return result;
    }

    private Object getvalue(JSONObject json, AggType aggType, String alias) {
        switch (aggType) {
            case COUNT:
                return json.get("doc_count");
            default:
                JSONObject jsonObject = json.getJSONObject(alias);
                if (jsonObject == null) {
                    return 0;
                }
                return jsonObject.get("value");
        }
    }

}
