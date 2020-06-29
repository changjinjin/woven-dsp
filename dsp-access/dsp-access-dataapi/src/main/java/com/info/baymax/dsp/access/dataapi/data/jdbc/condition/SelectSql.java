package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.jdbc.JdbcQuery;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ToString
@Slf4j
public class SelectSql implements Serializable {
    private static final long serialVersionUID = 9076946654365840665L;

    private SelectSql(JdbcQuery query) {
        build(StringUtils.join(query.finalSelectProperties(), ", "), query.getTable(),
            ConditionSql.build(query.getFieldGroup()), orderBy(query.getOrdSort()));
    }

    public static SelectSql build(JdbcQuery query) {
        return new SelectSql(query);
    }

    /**
     * 执行sql
     */
    @Getter
    private String executeSql;

    /**
     * 参数值列表
     */
    @Getter
    private Object[] paramValues = new Object[0];

    /**
     * 是否合格的查询
     */
    @Getter
    private boolean valid;

    private void build(String selectProperties, String table, ConditionSql conditionSql, String orderBy) {
        StringBuffer buf = new StringBuffer();
        buf.append("select ").append(selectProperties).append(" from ").append(table);
        if (conditionSql != null && StringUtils.isNotEmpty(conditionSql.getExecuteSql())) {
            buf.append(" where ").append(conditionSql.getExecuteSql()).append(" ");
        }
        if (StringUtils.isNotEmpty(orderBy)) {
            buf.append(" order by ").append(orderBy);
        }
        this.executeSql = buf.toString();
        this.paramValues = conditionSql.getParamValues();
        this.valid = true;

        log.debug("make executeSql:" + executeSql + ", paramValues:" + Arrays.toString(paramValues));
    }

    private String orderBy(List<Sort> ordSort) {
        if (ICollections.hasElements(ordSort)) {
            StringBuffer buf = new StringBuffer();
            buf.append(" ");
            for (Sort sort : ordSort) {
                buf.append(sort.getName()).append(" ").append(sort.getOrder()).append(", ");
            }
            return StringUtils.stripEnd(buf.toString().trim(), ",");
        }
        return "";
    }
}
