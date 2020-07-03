package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.utils.ICollections;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashSet;

@ToString
@Slf4j
public abstract class AbstractQuerySql<Q> implements Serializable {
    private static final long serialVersionUID = 9076946654365840665L;

    protected AbstractQuerySql(Q query) {
        build(query);
    }

    /**
     * 执行sql
     */
    @Getter
    @Setter
    protected String executeSql;

    /**
     * 参数值列表
     */
    @Getter
    @Setter
    protected Object[] paramValues = new Object[0];

    /**
     * 是否合格的查询
     */
    @Getter
    protected boolean valid;

    abstract void build(Q query);

    protected boolean valid(Q query) {
        this.valid = true;
        return this.valid;
    }

    protected String orderBy(LinkedHashSet<Sort> sorts) {
        String orderBy = "";
        if (ICollections.hasElements(sorts)) {
            StringBuffer buf = new StringBuffer();
            buf.append(" ");
            for (Sort sort : sorts) {
                buf.append(sort.getName()).append(" ").append(sort.getOrder()).append(", ");
            }
            orderBy = StringUtils.stripEnd(buf.toString().trim(), ",");
            log.debug("orderBy: order by " + orderBy);
        }
        return orderBy;
    }

    public void addParamValues(Object... values) {
        if (values != null && values.length > 0) {
            Object[] newValues = new Object[paramValues.length + values.length];
            System.arraycopy(paramValues, 0, newValues, 0, paramValues.length);
            System.arraycopy(values, 0, newValues, paramValues.length, values.length);
            this.paramValues = newValues;
        }
        log.debug("add param: " + Arrays.toString(values) + ",paramValues:" + Arrays.toString(paramValues));
    }
}
