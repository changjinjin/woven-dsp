package com.info.baymax.common.mybatis.mapper.extension;

import java.util.List;
import java.util.Map;

import tk.mybatis.mapper.annotation.RegisterMapper;
/**
 *  定义一些根据自定义特殊条件查询或者返回自定义ResultMap结果集的接口，继承该接口类需要在xml文件中定义对应的动态sql映射
 * @author jingwei.yang
 * @date 2019-05-28 15:03
 */
@RegisterMapper
public interface BaseExtensionSelectMapper<T> {

	/**
	 * 说明: 查询数据条数. <br>
	 * @param params
	 *            查询条件
	 * @return 符合条件的数据条数
	 */
	int selectExtensionCount(Map<?, ?> params);

	/**
	 * 说明: 查询符合条件的数据集合，其中返回的数据可能包含附加关联字段. <br>
	 * @param params
	 *            查询条件
	 * @return 符合条件的数据集合
	 */
	List<T> selectExtensionList(Map<?, ?> params);

	/**
	 * 说明: 根据条件查询唯一一条符合条件的记录. <br>
	 * @param params
	 *            查询条件，一般该条件最多能匹配到唯一一条数据
	 * @return 符合条件的唯一数据记录
	 */
	T selectExtensionOne(Map<?, ?> params);

}
