package com.merce.woven.common.mybatis.mapper.delete;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 根据条件进行批量删除
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:03
 */
@RegisterMapper
public interface DeleteListMapper<T> {

	/**
	 * 根据条件进行批量删除，依照方法参数集合中每个实体的非空字段为条件删除对应记录
	 *
	 * @param recordList
	 *            要删除的集合
	 * @return 影响的数据条数
	 */
	@DeleteProvider(type = DeleteListByPrimaryKeyProvider.class, method = "dynamicSQL")
	int deleteList(List<T> recordList);
}
