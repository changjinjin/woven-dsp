package com.info.baymax.common.queryapi.aggregate;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

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

	public List<String> getFinalSelectProperties() {
		List<String> selects = Lists.newArrayList();
		if (groupFields != null && !groupFields.isEmpty()) {
			selects.addAll(groupFields);
		}
		if (aggFields != null && !aggFields.isEmpty()) {
			selects.addAll(aggFields.stream().map(t -> t.toString()).collect(Collectors.toList()));
		}
		return Lists.newArrayList(selects);
	}
}
