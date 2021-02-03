package com.info.baymax.common.persistence.mybatis.mapper.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 根据主键字段进行批量删除
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:02
 */
@RegisterMapper
public interface DeleteListByPrimaryKeyMapper<T> {

    /**
     * 说明：根据主键字段进行批量删除，方法参数每个实体必须包含完整的主键属性. <br>
     *
     * @param recordList 要删除的集合
     * @return 影响的数据条数
     * @author jingwei.yang
     * @date 2018年1月4日 下午5:47:56
     */
    @DeleteProvider(type = DeleteListByPrimaryKeyProvider.class, method = "dynamicSQL")
    int deleteListByPrimaryKey(List<T> recordList);
}
