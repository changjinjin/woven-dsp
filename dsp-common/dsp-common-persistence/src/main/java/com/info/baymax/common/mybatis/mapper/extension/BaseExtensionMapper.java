package com.info.baymax.common.mybatis.mapper.extension;

import com.info.baymax.common.mybatis.mapper.MyBaseMapper;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 需要继承动态生成sql并且手动封装一些复杂关联查询sql的接口可以继承改接口，并实现自己的关联查询sql
 * @author jingwei.yang
 * @date 2019-05-28 15:03
 */
@RegisterMapper
public interface BaseExtensionMapper<T>
		extends //
			MyBaseMapper<T>, //
			BaseExtensionSelectMapper<T> //
{
}
