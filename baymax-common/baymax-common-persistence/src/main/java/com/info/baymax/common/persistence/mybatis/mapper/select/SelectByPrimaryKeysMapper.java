package com.info.baymax.common.persistence.mybatis.mapper.select;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 根据主键集合查询
 *
 * @param <T>
 * @author jingwei.yang
 * @date 2019-05-28 15:05
 */
@RegisterMapper
public interface SelectByPrimaryKeysMapper<T> {

    /**
     * 根据主键集合进行查询，类中只有存在一个带有@Id注解的字段. <br>
     *
     * @param keys 主键集合，对应放在sql中in条件中
     * @return 查询结果
     */
    @SelectProvider(type = SelectByPrimaryKeysProvider.class, method = "dynamicSQL")
    List<T> selectByPrimaryKeys(List<?> keys);

}
