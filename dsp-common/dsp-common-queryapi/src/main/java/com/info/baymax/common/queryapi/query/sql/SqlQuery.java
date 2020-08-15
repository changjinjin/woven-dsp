package com.info.baymax.common.queryapi.query.sql;

import com.info.baymax.common.queryapi.query.PageableQuery;
import com.info.baymax.common.queryapi.query.QueryBuilder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ApiModel
@Getter
public final class SqlQuery extends PageableQuery<SqlQuery>
        implements SqlQueryBuilder<SqlQuery>, QueryBuilder<SqlQuery> {

    @ApiModelProperty(value = "SQL模板", hidden = true)
    private String sqlTemplate;

    @ApiModelProperty(value = "参数列表", hidden = true)
    private List<Object> parameters;

    @ApiModelProperty(value = "参数列表")
    private Map<String, Object> parametersMap;

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
    public SqlQuery parameters(Collection<Object> parameters) {
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

}
