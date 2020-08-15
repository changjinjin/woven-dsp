package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RecordQuerySql extends AbstractQuerySql<JdbcQuery> {
    private static final long serialVersionUID = 9076946654365840665L;

    protected RecordQuerySql(JdbcQuery query) {
        super(query);
    }

    public static RecordQuerySql builder(JdbcQuery query) {
        return new RecordQuerySql(query);
    }

    protected void build(JdbcQuery query) {
        if (valid(query)) {
            String selectfromTableWhere = selectfromTableWhere(query.getTable(), query.getFieldGroup());
            this.placeholderCountSql = String.format(selectfromTableWhere, count());
            this.placeholderSql = String.format(
                new StringBuffer().append(selectfromTableWhere).append(orderBy(query.getOrdSort())).toString(),
                properties(query));
            log.debug("\n\nmake placeholderCountSql:" + placeholderCountSql);
            log.debug("make placeholderSql:" + placeholderSql);
            log.debug("make paramValues:" + Arrays.toString(paramValues) + "\n\n");
        }
    }

    @Override
    protected List<String> getSelectProperties(JdbcQuery query) {
        return query.getFinalSelectProperties();
    }

}
