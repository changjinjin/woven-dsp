package com.info.baymax.dsp.data.consumer.beans.source;

public enum RequestType {

    GET("GET"),
    POST("POST");

    private String value;

    RequestType(String type) {
        this.value = type;
    }

    public static boolean contains(String type) {
        for (RequestType typeEnum : RequestType.values()) {
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
