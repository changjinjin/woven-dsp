package com.info.baymax.common.queryapi.query.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel
@Getter
public class AggQuery extends AbstractAggQuery<AggQuery> {
    private static final long serialVersionUID = -878572549536122362L;

    public static AggQuery builder() {
        return new AggQuery();
    }

    public List<String> getFinalSelectProperties() {
        List<String> selects = new ArrayList<>();
        if (groupFields != null && !groupFields.isEmpty()) {
            selects.addAll(groupFields);
        }
        if (aggFields != null && !aggFields.isEmpty()) {
            selects.addAll(aggFields.stream().map(t -> t.toString()).collect(Collectors.toList()));
        }
        return new ArrayList<>(selects);
    }
}
