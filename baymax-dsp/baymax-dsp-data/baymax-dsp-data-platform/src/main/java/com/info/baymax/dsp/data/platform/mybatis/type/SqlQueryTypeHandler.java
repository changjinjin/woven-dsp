package com.info.baymax.dsp.data.platform.mybatis.type;

import com.info.baymax.common.persistence.mybatis.type.varchar.AbstractVarcharTypeHandler;
import com.info.baymax.common.queryapi.query.sql.SqlQuery;
import com.info.baymax.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

public class SqlQueryTypeHandler extends AbstractVarcharTypeHandler<SqlQuery> {

    @Override
    public String translate2Str(SqlQuery t) {
        return JsonUtils.toJson(t);
    }

    @Override
    public SqlQuery translate2Bean(String result) {
        if (StringUtils.isEmpty(result)) {
            return null;
        }
        return JsonUtils.fromJson(result, SqlQuery.class);
    }
}
