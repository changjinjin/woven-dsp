package com.info.baymax.dsp.data.consumer.beans.source;

public enum DataSourceType {
    DB("DB"),
    HTTP("HTTP"),
    SOCKET("SOCKET"),
    FTP("FTP"),
    SFTP("SFTP"),
    MONGODB("MONGODB"),
    ES("ES"),
    LOCALFS("LOCALFS"),
    KAFKA("KAFKA"),
    HDFS("HDFS");


    private String value;

    DataSourceType(String type) {
        this.value = type;
    }

    public static boolean contains(String type) {
        for (DataSourceType typeEnum : DataSourceType.values()) {
            if (typeEnum.value.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
