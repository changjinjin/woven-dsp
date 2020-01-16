package com.info.baymax.dsp.data.dataset.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class ConfigObject extends HashMap<String, Object> {
	private static final long serialVersionUID = -6971430229247641025L;

	public ConfigObject() {
        super();
    }

    public ConfigObject(Map o) {
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
