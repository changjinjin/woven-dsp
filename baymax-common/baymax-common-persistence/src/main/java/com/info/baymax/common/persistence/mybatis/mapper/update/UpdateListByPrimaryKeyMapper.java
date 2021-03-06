package com.info.baymax.common.persistence.mybatis.mapper.update;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 根据主键批量修改，需要指定每条数据的主键值，并且修改所有字段
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:06
 */
@RegisterMapper
public interface UpdateListByPrimaryKeyMapper<T> {

    /**
     * 说明：根据主键批量修改实体全部字段，null值会被更新。 <br>
     *
     * @param recordList 修改的数据列表
     * @return 影响的数据条数
     */
    @Options(useCache = false, useGeneratedKeys = false)
    @UpdateProvider(type = UpdateListByPrimaryKeyProvider.class, method = "dynamicSQL")
    int updateListByPrimaryKey(List<T> recordList);

}
