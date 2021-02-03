package com.info.baymax.common.persistence.mybatis.mapper.insert;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 批量插入，需要指定每条数据的主键值
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:04
 */
@RegisterMapper
public interface InsertListWithPrimaryKeyMapper<T> {

    /**
     * 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，该接口限制实体包含主键属性并且插入时指定主键值。 <br>
     *
     * @param recordList 插入的数据集
     * @return 影响的数据条数
     */
    @Options(useGeneratedKeys = false, keyProperty = "id")
    @InsertProvider(type = InsertListWithPrimaryKeyProvider.class, method = "dynamicSQL")
    int insertListWithPrimaryKey(List<T> recordList);

}
