package com.info.baymax.common.queryapi.record;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * rest 接口访问参数结构类
 *
 * @author jingwei.yang
 * @date 2020年6月24日 上午11:51:23
 */
@ApiModel
@Setter
@Getter
@ToString
public class RecordQuery extends AbstractPropertiesQuery<RecordQuery> implements Serializable {
	private static final long serialVersionUID = 5616355016961992353L;

	@ApiModelProperty(value = "所有的字段列表", hidden = true)
	private LinkedHashSet<String> allProperties;

	public static RecordQuery builder() {
		return new RecordQuery();
	}

	public static RecordQuery builder(RecordQuery query) {
		return query == null ? new RecordQuery() : query;
	}

	public RecordQuery allProperties(Collection<String> allProperties) {
		if (this.allProperties == null) {
			this.allProperties = Sets.newLinkedHashSet();
		}
		this.allProperties.addAll(allProperties);
		return this;
	}

	public RecordQuery allProperties(String... allProperties) {
		return allProperties(Arrays.asList(allProperties));
	}

	@Override
	public List<String> getFinalSelectProperties() {
		Set<String> select = Sets.newLinkedHashSet();
		if (allProperties != null && !allProperties.isEmpty()) {
			Set<String> all = Sets.newLinkedHashSet(allProperties);
			if (excludeProperties != null && !excludeProperties.isEmpty()) {
				Set<String> exclude = Sets.newLinkedHashSet(excludeProperties);
				all.removeAll(exclude);
			}
			if (selectProperties != null && !selectProperties.isEmpty()) {
				select = Sets.newLinkedHashSet(selectProperties);
				select.retainAll(all);
			} else {
				select.addAll(all);
			}
		} else {
			select.addAll(selectProperties);
		}
		if (select == null || select.isEmpty()) {
			throw new RuntimeException("no suitable fields for query with allProperties:" + allProperties
					+ ", selectProperties:" + selectProperties + ",excludeProperties:" + excludeProperties);
		}
		return Lists.newArrayList(select);
	}
}
