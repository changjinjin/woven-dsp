package com.info.baymax.common.service.criteria.agg;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@ApiModel
@Getter
public class AggQuery extends AbstractAggQuery<AggQuery> {
    private static final long serialVersionUID = -878572549536122362L;

    @ApiModelProperty(value = "表名称", hidden = true)
    private String table;

    public static AggQuery builder() {
        return new AggQuery();
    }

    public AggQuery table(String table) {
        this.table = table;
        return this;
    }

    public List<String> finalSelectProperties() {
        List<String> selects = Lists.newArrayList();
        if (ICollections.hasElements(groupFields)) {
            selects.addAll(groupFields);
        }
        if (ICollections.hasElements(aggFields)) {
            selects.addAll(aggFields.stream().map(t -> t.toString()).collect(Collectors.toList()));
        }
        return selects;
    }

    public static void main(String[] args) {
        AggQuery query = AggQuery.builder()//
            .fieldGroup(FieldGroup.builder().andGreaterThan("avg_name", 2))//
            .orderBy("avg_name")//
            .aggField("age", AggType.AVG)//
            .aggField("name", AggType.COUNT)//
            .groupField("class")//
            ;
        System.out.println(JSON.toJSONString(query));
    }

}
