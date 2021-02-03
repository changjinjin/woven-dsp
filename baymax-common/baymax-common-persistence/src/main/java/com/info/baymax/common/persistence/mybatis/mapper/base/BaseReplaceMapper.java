package com.info.baymax.common.persistence.mybatis.mapper.base;

import com.info.baymax.common.persistence.mybatis.mapper.insert.ReplaceListWithPrimaryKeyMapper;
import com.info.baymax.common.persistence.mybatis.mapper.insert.ReplaceMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;

/**
 * 插入或更新接口，该接口类适用于支持覆盖插入的数据库，如果插入的数据表存在单个主键或者联合主键，当插入的数据在数据库中存在对应主键的记录时新数据覆盖原来的记录，否则插入新的记录.
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:01
 */
@RegisterMapper
public interface BaseReplaceMapper<T>
    extends//
    ReplaceMapper<T>, //
    ReplaceListWithPrimaryKeyMapper<T>//
{

}
