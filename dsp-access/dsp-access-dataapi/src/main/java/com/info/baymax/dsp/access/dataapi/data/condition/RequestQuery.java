package com.info.baymax.dsp.access.dataapi.data.condition;

import com.info.baymax.common.service.criteria.query.AbstractQuery;
import com.inforefiner.repackaged.com.google.common.collect.Sets;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * rest 接口访问参数结构类
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:51:23
 */
@ApiModel
@Getter
@ToString
public class RequestQuery extends AbstractQuery<RequestQuery> implements Serializable {
    private static final long serialVersionUID = 5616355016961992353L;

    @ApiModelProperty(value = "表名称", hidden = true)
    private String table;
    @ApiModelProperty(value = "所有的字段列表", hidden = true)
    private Set<String> allProperties;

    /*************************************
     * 建造器
     *****************************************/
    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static RequestQuery builder() {
        return new RequestQuery();
    }

    /**
     * 创建一个Query的构建器
     *
     * @return 默认的构建器
     */
    public static RequestQuery builder(RequestQuery query) {
        return query == null ? new RequestQuery() : query;
    }

    public RequestQuery table(String table) {
        this.table = table;
        return this;
    }

    public RequestQuery allProperties(Collection<String> allProperties) {
        if (this.allProperties == null) {
            this.allProperties = Sets.newLinkedHashSet();
        }
        this.allProperties.addAll(allProperties);
        return this;
    }

    public RequestQuery allProperties(String... allProperties) {
        if (this.allProperties == null) {
            this.allProperties = Sets.newLinkedHashSet();
        }
        this.allProperties.addAll(Arrays.asList(allProperties));
        return this;
    }
}
