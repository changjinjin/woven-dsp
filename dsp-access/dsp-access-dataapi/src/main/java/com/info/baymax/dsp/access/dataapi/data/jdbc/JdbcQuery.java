package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.info.baymax.common.service.criteria.query.RecordQuery;
import com.info.baymax.common.utils.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

/**
 * rest 接口访问参数结构类
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:51:23
 */
@ApiModel
@Getter
@ToString
public class JdbcQuery extends RecordQuery {
    private static final long serialVersionUID = 5616355016961992353L;

    @JsonIgnore
    @ApiModelProperty(value = "表名称", hidden = true)
    private String table;

    public JdbcQuery table(String table) {
        this.table = table;
        return this;
    }

    public static JdbcQuery from(RecordQuery query)
        throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        return JsonUtils.fromObject(query, new TypeReference<JdbcQuery>() {
        });
    }
}
