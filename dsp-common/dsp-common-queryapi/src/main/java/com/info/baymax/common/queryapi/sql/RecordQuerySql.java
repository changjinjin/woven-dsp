package com.info.baymax.common.queryapi.sql;

import com.info.baymax.common.queryapi.query.record.RecordQuery;

import java.util.List;

public class RecordQuerySql extends AbstractQuerySql<RecordQuery> {

    protected RecordQuerySql(RecordQuery query) {
        super(query);
    }

    public static RecordQuerySql builder(RecordQuery query) {
        return new RecordQuerySql(query);
    }

    protected void build(RecordQuery query) {
        if (valid(query)) {
            String selectfromTableWhere = selectfromTableWhere(query.getTable(), query.getFieldGroup());
            this.placeholderCountSql = String.format(selectfromTableWhere, count());
            this.placeholderSql = String.format(
                    new StringBuffer().append(selectfromTableWhere).append(orderBy(query.getOrdSort())).toString(),
                    properties(query));
        }
    }

    @Override
    protected List<String> getSelectProperties(RecordQuery query) {
        return query.getFinalSelectProperties();
    }

}
