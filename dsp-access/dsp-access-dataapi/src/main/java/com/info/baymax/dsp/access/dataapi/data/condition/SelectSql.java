package com.info.baymax.dsp.access.dataapi.data.condition;

import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.DataReadException;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ToString
@Slf4j
public class SelectSql implements Serializable {
    private static final long serialVersionUID = 9076946654365840665L;

    private SelectSql(RequestQuery query) {
        build(selectProperties(query.getAllProperties(), query.getSelectProperties(), query.getExcludeProperties()),
            query.getTable(), ConditionSql.build(query.getFieldGroup()), orderBy(query.getOrdSort()));
    }

    public static SelectSql build(RequestQuery query) {
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

    private String selectProperties(Set<String> allProperties, Set<String> selectProperties,
                                    Set<String> excludeProperties) {
        if (ICollections.hasElements(allProperties)) {
            if (ICollections.hasElements(excludeProperties)) {
                allProperties.removeAll(excludeProperties);
            }
            if (ICollections.hasElements(selectProperties)) {
                selectProperties.retainAll(allProperties);
            }
        }
        if (ICollections.hasNoElements(selectProperties)) {
            throw new DataReadException("no suitable fields for query with allProperties:" + allProperties
                + ", selectProperties:" + selectProperties + ",excludeProperties:" + excludeProperties);
        }
        return StringUtils.join(selectProperties, ", ");
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
