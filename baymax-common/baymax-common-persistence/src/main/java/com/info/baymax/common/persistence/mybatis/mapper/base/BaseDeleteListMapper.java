package com.info.baymax.common.persistence.mybatis.mapper.base;

import com.info.baymax.common.persistence.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.info.baymax.common.persistence.mybatis.mapper.delete.DeleteListByPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.delete.DeleteListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 批量删除接口
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:00
 */
@RegisterMapper
public interface BaseDeleteListMapper<T> extends//
    DeleteListMapper<T>, //
    DeleteListByPrimaryKeyMapper<T>, //
    DeleteByPrimaryKeysMapper<T>//
{

}
