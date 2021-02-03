package com.info.baymax.common.persistence.mybatis.mapper;

import com.info.baymax.common.persistence.mybatis.mapper.delete.DeleteByPrimaryKeysMapper;
import com.info.baymax.common.persistence.mybatis.mapper.delete.DeleteListByPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.insert.InsertListWithPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.insert.ReplaceListWithPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.insert.ReplaceMapper;
import com.info.baymax.common.persistence.mybatis.mapper.select.SelectByPrimaryKeysMapper;
import com.info.baymax.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.update.UpdateListByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.base.delete.DeleteByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.ExistsWithPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.select.SelectByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeyMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 存在主键的实体根据主键参数进行增删改查操作接口.
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:07
 */
@RegisterMapper
public interface MyIdableMapper<T>
    extends //
    InsertListWithPrimaryKeyMapper<T>, //
    InsertUseGeneratedKeysMapper<T>, //
    ReplaceMapper<T>, //
    ReplaceListWithPrimaryKeyMapper<T>, //
    UpdateByPrimaryKeyMapper<T>, //
    UpdateByPrimaryKeySelectiveMapper<T>, //
    UpdateListByPrimaryKeyMapper<T>, //
    UpdateListByPrimaryKeySelectiveMapper<T>, //
    DeleteByPrimaryKeyMapper<T>, //
    DeleteByPrimaryKeysMapper<T>, //
    DeleteListByPrimaryKeyMapper<T>, //
    SelectByPrimaryKeyMapper<T>, //
    SelectByPrimaryKeysMapper<T>, //
    IdsMapper<T>, //
    ExistsWithPrimaryKeyMapper<T>, //
    MyBaseMapper<T> //
{
}
