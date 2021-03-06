package com.info.baymax.common.queryapi.query.record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * rest 接口访问参数结构类
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:51:23
 */
@ApiModel
@Getter
public class RecordQuery extends AbstractPropertiesQuery<RecordQuery> implements Serializable {
    private static final long serialVersionUID = 5616355016961992353L;

    @ApiModelProperty(value = "所有的字段列表", hidden = true, required = false)
    private LinkedHashSet<String> allProperties;

    public static RecordQuery builder() {
        return new RecordQuery();
    }

    public static RecordQuery builder(RecordQuery query) {
        return query == null ? builder() : query;
    }

    public RecordQuery allProperties(Collection<String> allProperties) {
        if (this.allProperties == null) {
            this.allProperties = new LinkedHashSet<>();
        }
        this.allProperties.addAll(allProperties);
        return this;
    }

    public RecordQuery allProperties(String... allProperties) {
        return allProperties(Arrays.asList(allProperties));
    }

    @Override
    public List<String> getFinalSelectProperties() {
        Set<String> select = new LinkedHashSet<>();
        if (allProperties != null && !allProperties.isEmpty()) {
            Set<String> all = new LinkedHashSet<>(allProperties);
            if (excludeProperties != null && !excludeProperties.isEmpty()) {
                Set<String> exclude = new LinkedHashSet<>(excludeProperties);
                all.removeAll(exclude);
            }
            if (selectProperties != null && !selectProperties.isEmpty()) {
                select = new LinkedHashSet<>(selectProperties);
                select.retainAll(all);
            } else {
                select.addAll(all);
            }
        } else {
            select.addAll(selectProperties);
        }
        if (select == null || select.isEmpty()) {
            throw new RuntimeException("no suitable fields for query with allProperties:" + allProperties
                + ", selectProperties:" + selectProperties + ",excludeProperties:" + excludeProperties);
        }
        return new ArrayList<String>(select);
    }
}
