package com.info.baymax.dsp.access.dataapi.data;

import com.google.common.collect.Sets;
import com.info.baymax.common.service.criteria.query.AbstractPropertiesQuery;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * rest 接口访问参数结构类
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:51:23
 */
@ApiModel
@Setter
@Getter
@ToString
public class RecordQuery extends AbstractPropertiesQuery<RecordQuery> implements Serializable {
    private static final long serialVersionUID = 5616355016961992353L;

    @ApiModelProperty(value = "所有的字段列表", hidden = true)
    private LinkedHashSet<String> allProperties;

    public static RecordQuery builder() {
        return new RecordQuery();
    }

    public static RecordQuery builder(RecordQuery query) {
        return query == null ? new RecordQuery() : query;
    }

    public RecordQuery allProperties(Collection<String> allProperties) {
        if (this.allProperties == null) {
            this.allProperties = Sets.newLinkedHashSet();
        }
        this.allProperties.addAll(allProperties);
        return this;
    }

    public RecordQuery allProperties(String... allProperties) {
        return allProperties(Arrays.asList(allProperties));
    }

    public Set<String> finalSelectProperties() {
        Set<String> select = Sets.newHashSet();
        if (ICollections.hasElements(allProperties)) {
            Set<String> all = Sets.newHashSet(allProperties);
            if (ICollections.hasElements(excludeProperties)) {
                Set<String> exclude = Sets.newHashSet(excludeProperties);
                all.removeAll(exclude);
            }
            if (ICollections.hasElements(selectProperties)) {
                select = Sets.newHashSet(selectProperties);
                select.retainAll(all);
            }
        }
        if (ICollections.hasNoElements(select)) {
            throw new DataReadException("no suitable fields for query with allProperties:" + allProperties
                + ", selectProperties:" + selectProperties + ",excludeProperties:" + excludeProperties);
        }
        return select;
    }
}
