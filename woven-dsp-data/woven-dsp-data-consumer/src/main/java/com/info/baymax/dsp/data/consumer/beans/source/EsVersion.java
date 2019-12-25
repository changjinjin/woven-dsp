package com.info.baymax.dsp.data.consumer.beans.source;

public enum EsVersion {
    ONE("5.x");

    private String value;

    EsVersion(String version) {
        this.value = version;
    }

    public static boolean contains(String type) {
        for (EsVersion typeEnum : EsVersion.values()) {
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
