package com.info.baymax.common.jpa.criteria.query;

import com.info.baymax.common.jpa.criteria.query.JpaCriteriaHelper.OrderDirection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 排序属性包装
 *
 * @author jingwei.yang
 * @date 2019年5月5日 下午12:10:29
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@Deprecated
public class SortObject implements Serializable {
	private static final long serialVersionUID = -8413317033449069620L;

	/**
	 * 属性名
	 */
	@ApiModelProperty("属性名")
	private String field;

	/**
	 * 排序类型
	 */
	@ApiModelProperty("排序类型")
	private OrderDirection orderDirection;

	public SortObject(String field) {
		this(field, OrderDirection.ASC);
	}

}
