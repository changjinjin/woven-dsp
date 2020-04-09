package com.info.baymax.dsp.data.consumer.beans.source;

/**
 * @Author: haijun
 * @Date: 2020/4/9 17:20
 */
public enum KafkaFormat {
    TXT("txt"),
    CSV("csv"),
    JSON("json"),
    AVRO("avro");

    private final String value;

    KafkaFormat(final String value) {
        this.value = value;
    }

    public static KafkaFormat fromString(String text) {
        for (KafkaFormat f : KafkaFormat.values()) {
            if (f.value.equalsIgnoreCase(text)) {
                return f;
            }
        }
        return null;
    }

    public static boolean contains(String type){
        for(KafkaFormat typeEnum : KafkaFormat.values()){
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
