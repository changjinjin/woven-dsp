package com.info.baymax.dsp.data.consumer.beans.source;

public enum ProtocolType {

    TCP("TCP"),
    UDP("UDP");

    private String value;

    ProtocolType(String type) {
        this.value = type;
    }

    public static boolean contains(String type) {
        for (ProtocolType typeEnum : ProtocolType.values()) {
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

