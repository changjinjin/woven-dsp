package com.info.baymax.dsp.data.dataset.entity.security;


import com.info.baymax.common.utils.ICollections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 资源目录类型枚举
 *
 * @author jingwei.yang
 * @date 2019年9月11日 下午9:01:37
 */
public enum ResourceType {
    flow_dir("merce_flow"), dataset_dir("merce_dataset"), schema_dir("merce_schema"), datasource_dir("merce_dss"),
    standard_dir("merce_sdb");

    /**
     * 对应所属权限编码
     */
    private final String table;

    private ResourceType(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    public static Map<String, ResourceType> getTypeMap() {
        Map<String, ResourceType> map = new HashMap<>();
        for (ResourceType type : values()) {
            map.put(type.name(), type);
        }
        return map;
    }

    public static ResourceType getByTable(String tableName) {
        for (ResourceType type : values()) {
            if (type.getTable().equalsIgnoreCase(tableName)) {
                return type;
            }
        }
        return null;
    }

    public static ResourceType getByType(String resType) {
        return getTypeMap().get(resType);
    }

    public static String[] getTypes() {
        return Stream.of(values()).map(t -> t.name()).toArray(String[]::new);
    }

    public static String[] getCodes(String[] includes, boolean mode) {
        if (includes == null || includes.length == 0) {
            return getTypes();
        }

        if (mode) {
            return includes;
        } else {
            List<String> allCodes = Arrays.asList(getTypes());
            allCodes.removeAll(Arrays.asList(includes));
            includes = ICollections.toArray(allCodes);
            return includes.length == 0 ? getTypes() : includes;
        }
    }
}
