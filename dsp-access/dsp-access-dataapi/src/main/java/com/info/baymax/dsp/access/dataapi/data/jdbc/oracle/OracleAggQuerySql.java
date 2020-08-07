package com.info.baymax.dsp.access.dataapi.data.jdbc.oracle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.info.baymax.common.service.criteria.agg.AggField;
import com.info.baymax.common.service.criteria.agg.AggQuery;
import com.info.baymax.common.service.criteria.field.Field;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.access.dataapi.data.jdbc.condition.AggQuerySql;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ToString(callSuper = true)
public class OracleAggQuerySql extends AggQuerySql {
    private static final long serialVersionUID = 9076946654365840665L;

    protected OracleAggQuerySql(AggQuery query) {
        super(query);
    }

    public static OracleAggQuerySql builder(AggQuery query) {
        return new OracleAggQuerySql(query);
    }

    @Override
    protected void build(AggQuery query) {
        super.build(preBuild(query));
    }

    // Oracle having 不能使用聚合的别名作为条件，这里加having中的条件替换成聚合表达式
    private AggQuery preBuild(AggQuery query) {
        LinkedHashSet<AggField> aggFields = query.getAggFields();
        Map<String, String> aggFieldMap = Maps.newHashMap();
        for (AggField aggField : aggFields) {
            aggFieldMap.put(aggField.getAlias(), aggField.getAggExpr());
        }
        query.setHavingFieldGroup(
            replaceHavingFieldGroupAlias(aggFieldMap.keySet(), aggFieldMap, query.getHavingFieldGroup()));
        query.setOrdSort(replaceOrdSortAlias(aggFieldMap.keySet(), aggFieldMap, query.getOrdSort()));
        return query;
    }

    private FieldGroup replaceHavingFieldGroupAlias(Set<String> keySet, Map<String, String> aggFieldMap,
                                                    FieldGroup havingFieldGroup) {
        if (havingFieldGroup != null) {
            List<Field> fields = havingFieldGroup.getFields();
            if (ICollections.hasElements(fields)) {
                for (Field field : fields) {
                    if (keySet.contains(field.getName())) {
                        field.setName(aggFieldMap.get(field.getName()));
                    }
                }
            }
            List<FieldGroup> fieldGroups = havingFieldGroup.getFieldGroups();
            if (ICollections.hasElements(fieldGroups)) {
                for (FieldGroup fieldGroup : fieldGroups) {
                    replaceHavingFieldGroupAlias(keySet, aggFieldMap, fieldGroup);
                }
            }
        }
        return havingFieldGroup;
    }

    private LinkedHashSet<Sort> replaceOrdSortAlias(Set<String> keySet, Map<String, String> aggFieldMap,
                                                    LinkedHashSet<Sort> ordSort) {
        if (ICollections.hasElements(ordSort)) {
            for (Sort sort : ordSort) {
                String name = sort.getName();
                if (keySet.contains(name)) {
                    sort.setName(aggFieldMap.get(name));
                }
            }
        }
        return ordSort;
    }

    // oracle 字段别名需要加引号
    @Override
    protected List<String> getSelectProperties(AggQuery query) {
        LinkedHashSet<String> groupFields = query.getGroupFields();
        List<String> selects = Lists.newArrayList();
        if (ICollections.hasElements(groupFields)) {
            selects.addAll(groupFields);
        }

        LinkedHashSet<AggField> aggFields = query.getAggFields();
        for (AggField aggField : aggFields) {
            aggField.setAlias(wrapField(aggField.getAlias()));
        }
        if (ICollections.hasElements(aggFields)) {
            selects.addAll(aggFields.stream().map(t -> t.toString()).collect(Collectors.toList()));
        }
        return Lists.newArrayList(selects);

    }

    private String wrapField(String field) {
        if (!field.startsWith("\"")) {
            return "\"" + field + "\"";
        }
        return field;
    }

}
