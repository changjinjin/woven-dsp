package com.info.baymax.common.queryapi.query.sql;

import com.info.baymax.common.queryapi.query.PageableQuery;
import com.info.baymax.common.queryapi.query.QueryBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@ApiModel
@Getter
public final class SqlQuery extends PageableQuery<SqlQuery>
    implements SqlQueryBuilder<SqlQuery>, QueryBuilder<SqlQuery> {

    @ApiModelProperty(value = "SQL模板")
    private String sqlTemplate;

    @ApiModelProperty(value = "参数列表")
    private List<Parameter> parameters;

    public static SqlQuery builder() {
        return new SqlQuery();
    }

    public static SqlQuery builder(SqlQuery query) {
        return query == null ? builder() : query;
    }

    @Override
    public SqlQuery sqlTemplate(String sqlTemplate) {
        this.sqlTemplate = sqlTemplate;
        return this;
    }

    @Override
    public SqlQuery clearSqlTemplate() {
        this.sqlTemplate = null;
        return this;
    }

    @Override
    public SqlQuery parameters(Collection<Parameter> parameters) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters.addAll(parameters);
        }
        return this;
    }

    @Override
    public SqlQuery clearParameters() {
        if (parameters != null && !parameters.isEmpty()) {
            parameters.clear();
        }
        return this;
    }

    @Override
    public SqlQuery clear() {
        clearPageable();
        clearParameters();
        return this;
    }

    public Map<String, Object> getParametersMap() {
        HashMap<String, Object> parametersMap = new HashMap<String, Object>();
        if (parameters != null && !parameters.isEmpty()) {
            for (Parameter parameter : parameters) {
                parametersMap.put(parameter.getName(), parameter.getValue());
            }
        }
        return parametersMap;
    }

}

@ApiModel
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Parameter implements Serializable {
    private static final long serialVersionUID = -2410834598362893279L;

    @ApiModelProperty("参数名称")
    private String name;

    @ApiModelProperty("参数值")
    private Object value;

    @ApiModelProperty("默认值")
    private Object defaultValue;

    @ApiModelProperty("参数注释")
    private String content;
}
