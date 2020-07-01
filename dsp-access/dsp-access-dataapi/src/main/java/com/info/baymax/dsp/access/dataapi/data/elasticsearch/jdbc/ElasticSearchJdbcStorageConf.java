package com.info.baymax.dsp.access.dataapi.data.elasticsearch.jdbc;

import com.info.baymax.dsp.access.dataapi.data.Engine;
import com.info.baymax.dsp.access.dataapi.data.elasticsearch.ElasticSearchStorageConf;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcStorageConf;
import com.info.baymax.dsp.data.consumer.beans.source.DBType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true, doNotUseGetters = true)
public class ElasticSearchJdbcStorageConf extends JdbcStorageConf {
    private static final long serialVersionUID = -187465371872121001L;

    private ElasticSearchStorageConf storageConf;

    public ElasticSearchJdbcStorageConf(ElasticSearchStorageConf storageConf) {
        this.storageConf = storageConf;
    }

    @Override
    public Engine getEngine() {
        return Engine.ELASTICSEARCH;
    }

    @Override
    public String getBatchsize() {
        return "1000";
    }

    @Override
    public String getDBType() {
        return DBType.Elasticsearch.name();
    }

    @Override
    public String getDriver() {
        return "com.amazon.opendistroforelasticsearch.jdbc.ElasticsearchDriver";
    }

    @Override
    public String getHost() {
        return storageConf.getIpAddresses();
    }

    @Override
    public String getPort() {
        return "9200";
    }

    @Override
    public String getName() {
        return storageConf.getClusterName();
    }

    @Override
    public String getResType() {
        return DBType.Elasticsearch.name();
    }

    @Override
    public String getDatabase() {
        return storageConf.getIndex();
    }

    @Override
    public String getTable() {
        return storageConf.getIndexType();
    }

    @Override
    public String getUrl() {
        return "jdbc:elasticsearch://" + storageConf.getIpAddresses();
    }

    @Override
    public String getUser() {
        return storageConf.getUsername();
    }

    @Override
    public String getUsername() {
        return storageConf.getUsername();
    }

    @Override
    public String getPassword() {
        return storageConf.getPassword();
    }

}
