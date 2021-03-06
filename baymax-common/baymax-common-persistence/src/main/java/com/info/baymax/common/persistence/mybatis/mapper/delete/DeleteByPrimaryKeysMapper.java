package com.info.baymax.common.persistence.mybatis.mapper.delete;

import org.apache.ibatis.annotations.DeleteProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 根据主键集合进行批量删除
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:02
 */
@RegisterMapper
public interface DeleteByPrimaryKeysMapper<T> {

    /**
     * 说明： 根据主键集合进行批量删除，参数是主键的集合数据. <br>
     *
     * @param keys 要删除的主键集合
     * @return 影响的数据条数
     * @author jingwei.yang
     * @date 2018年1月4日 下午5:47:01
     */
    @DeleteProvider(type = DeleteByPrimaryKeysProvider.class, method = "dynamicSQL")
    int deleteByPrimaryKeys(List<?> keys);
}
