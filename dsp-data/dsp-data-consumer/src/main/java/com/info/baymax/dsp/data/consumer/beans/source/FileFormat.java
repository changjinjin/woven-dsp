package com.info.baymax.dsp.data.consumer.beans.source;

/**
 * @Author: haijun
 * @Date: 2020/4/9 17:20
 */
public enum FileFormat {
    TXT("txt"),
    CSV("csv"),
    PARQUET("parquet"),
    JSON("json"),
    ORC("orc"),
    AVRO("avro");

    private final String value;

    FileFormat(final String value) {
        this.value = value;
    }

    public static FileFormat fromString(String text) {
        for (FileFormat f : FileFormat.values()) {
            if (f.value.equalsIgnoreCase(text)) {
                return f;
            }
        }
        return null;
    }

    public static boolean contains(String type){
        for(FileFormat typeEnum : FileFormat.values()){
            if(typeEnum.value.equalsIgnoreCase(type)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
