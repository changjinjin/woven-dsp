package com.info.baymax.dsp.data.consumer.beans.source;

public enum TrueOrFalse {
    TRUE("true"),
    FALSE("false");

    private String value;

    TrueOrFalse(String storageType){
        this.value = storageType;
    }

    public static boolean contains(String type){
        for(TrueOrFalse typeEnum : TrueOrFalse.values()){
            if(typeEnum.value.equals(type)){
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
