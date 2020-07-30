package com.info.baymax.dsp.access.dataapi.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.utils.JsonUtils;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.ElasticSearchStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "storage", visible = true)
@JsonSubTypes({@JsonSubTypes.Type(value = JdbcStorageConf.class, name = "JDBC"),
    @JsonSubTypes.Type(value = ElasticSearchStorageConf.class, name = "ElasticSearch")})
@Slf4j
@Setter
@Getter
public class StorageConf implements Serializable {
    private static final long serialVersionUID = 3126612346037768215L;

    protected String storage;

    public Engine getEngine() {
        return Engine.valueOf(getStorage().toUpperCase());
    }

    public static StorageConf from(Map<String, Object> conf) {
        try {
            return JsonUtils.fromObject(conf, new TypeReference<StorageConf>() {
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String toString() {
        return "StorageConf [storage=" + storage + "]";
    }
}
