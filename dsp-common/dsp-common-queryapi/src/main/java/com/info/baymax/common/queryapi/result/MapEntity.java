package com.info.baymax.common.queryapi.result;

import java.util.HashMap;
import java.util.Map;

public class MapEntity extends HashMap<String, Object> {
    private static final long serialVersionUID = -639399742310897905L;

    public static MapEntity from(Map<String, Object> m) {
        if (m != null) {
            return MapEntity.build().addAll(m);
        }
        return null;
    }

    public static MapEntity build() {
        return new MapEntity();
    }

    public MapEntity add(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public MapEntity addAll(Map<String, Object> m) {
        this.putAll(m);
        return this;
    }
}
