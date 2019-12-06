package com.merce.woven.common.mybatis.mapper.extension;

import com.merce.woven.common.mybatis.mapper.MyIdableMapper;

import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 存在ID属性的实体持久层接口类，一并实现扩展的查询接口
 * @author jingwei.yang
 * @date 2019-05-28 15:04
 */
@RegisterMapper
public interface BaseIdableExtensionMapper<T>
		extends //
			MyIdableMapper<T>, //
			BaseExtensionSelectMapper<T> //
{
}
