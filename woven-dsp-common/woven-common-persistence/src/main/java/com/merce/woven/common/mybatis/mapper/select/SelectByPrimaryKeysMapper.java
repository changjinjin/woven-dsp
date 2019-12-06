package com.merce.woven.common.mybatis.mapper.select;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 根据主键集合查询
 * @author jingwei.yang
 * @date 2019-05-28 15:05
 * @param <T>
 */
@RegisterMapper
public interface SelectByPrimaryKeysMapper<T> {

	/**
	 * 根据主键集合进行查询，类中只有存在一个带有@Id注解的字段. <br>
	 * @param keys
	 *            主键集合，对应放在sql中in条件中
	 * @return 查询结果
	 */
	@SelectProvider(type = SelectByPrimaryKeysProvider.class, method = "dynamicSQL")
	List<T> selectByPrimaryKeys(List<?> keys);

}
