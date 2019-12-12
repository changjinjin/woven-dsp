package com.merce.woven.dsp.data.sys.entity.bean;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModel;

@ApiModel
public class ConfigObject extends HashMap<String, Object> {
    private static final long serialVersionUID = 5848024528445843806L;

    public ConfigObject() {
        super();
    }

    public ConfigObject(Map<String, Object> o) {
        super();
        if (o != null) {
            this.putAll(o);
        }
    }

    public ConfigObject withConfig(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
