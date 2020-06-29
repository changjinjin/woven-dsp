package com.info.baymax.dsp.access.dataapi.data.jdbc;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.dsp.access.dataapi.data.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

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
public class JdbcQuery extends Query {
    private static final long serialVersionUID = 5616355016961992353L;

    @ApiModelProperty(value = "表名称", hidden = true)
    private String table;

    public JdbcQuery table(String table) {
        this.table = table;
        return this;
    }

    public static JdbcQuery from(Query query)
        throws JsonParseException, JsonMappingException, JsonProcessingException, IOException {
        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        return mapper.readValue(mapper.writeValueAsBytes(query), new TypeReference<JdbcQuery>() {
        });
    }
}
