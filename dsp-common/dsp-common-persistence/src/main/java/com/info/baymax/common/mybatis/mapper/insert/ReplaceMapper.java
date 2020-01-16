package com.info.baymax.common.mybatis.mapper.insert;

import org.apache.ibatis.annotations.InsertProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 *  覆盖插入一条数据
 * @author jingwei.yang
 * @date 2019-05-28 15:05
 * @param <T>
 */
@RegisterMapper
public interface ReplaceMapper<T> {

	/**
	 * 说明：使用覆盖的方式插入一条记录，null的属性也会保存，不会使用数据库默认值。假如表中的一个旧记录与一个用于PRIMARY
	 * KEY或一个UNIQUE索引的新记录具有相同的值，则在新记录被插入之前，旧记录被删除。 注意，除非表有一个PRIMARY
	 * KEY或UNIQUE索引，否则，使用一个REPLACE语句没有意义。该语句会与INSERT相同，因为没有索引被用于确定是否新行复制了其它的行。
	 * <br>
	 * @param record
	 *            插入的数据记录
	 * @return 受影响的数据条数
	 */
	@InsertProvider(type = ReplaceProvider.class, method = "dynamicSQL")
	int replace(T record);

}
