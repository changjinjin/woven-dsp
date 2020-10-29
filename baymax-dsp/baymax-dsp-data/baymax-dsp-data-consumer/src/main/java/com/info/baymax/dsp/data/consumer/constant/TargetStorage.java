package com.info.baymax.dsp.data.consumer.constant;

/**
 * @Author: haijun
 * @Date: 2019/12/19 17:46
 */
public enum TargetStorage {
    ELASTICSEARCH("ElasticSearch"),
    HBASE("HBASE"),
    HIVE("HIVE"),
    HDFS("HDFS"),
    JDBC("JDBC"),
    REDIS("REDIS"),
    KAFKA("KAFKA"),
    NEO4J("Neo4j"),
    S3A("S3A");

    private final String value;

    TargetStorage(final String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
