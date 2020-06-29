package com.info.baymax.dsp.access.dataapi.data.elasticsearch;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.dsp.access.dataapi.data.Query;
import com.inforefiner.repackaged.org.apache.curator.shaded.com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class ElasticsearchQuery extends Query {
    private static final long serialVersionUID = -3987015051297973072L;

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 索引名称列表
     */
    private List<String> indices;

    /**
     * 索引类型列表
     */
    private List<String> indexTypes;

    public ElasticsearchQuery clusterName(String clusterName) {
        this.clusterName = clusterName;
        return this;
    }

    public ElasticsearchQuery index(String index) {
        return indices(index);
    }

    public ElasticsearchQuery indices(String... indices) {
        return indices(Arrays.asList(indices));
    }

    public ElasticsearchQuery indices(Collection<String> indices) {
        if (this.indices == null) {
            this.indices = Lists.newArrayList();
        }
        this.indices.addAll(indices);
        return this;
    }

    public ElasticsearchQuery indexType(String indexType) {
        return indexTypes(indexType);
    }

    public ElasticsearchQuery indexTypes(String... indexTypes) {
        return indexTypes(Arrays.asList(indexTypes));
    }

    public ElasticsearchQuery indexTypes(Collection<String> indexTypes) {
        if (this.indexTypes == null) {
            this.indexTypes = Lists.newArrayList();
        }
        this.indexTypes.addAll(indexTypes);
        return this;
    }

    public static ElasticsearchQuery from(Query query)
        throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        return mapper.readValue(mapper.writeValueAsBytes(query), new TypeReference<ElasticsearchQuery>() {
        });
    }

}
