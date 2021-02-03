package com.info.baymax.common.persistence.mybatis.mapper.update;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface UpdateByPrimaryKeySelectiveWithForceVersionMapper<T> {

    /**
     * 根据主键更新部分字段并且可强制更新version字段
     *
     * @param record       更新的数据记录
     * @param forceVersion 是否需要刷新version字段
     * @return 更新数据条数，如果为0则更新失败，使用乐观锁时可根据该值判断成功与否
     */
    @UpdateProvider(type = UpdateByPrimaryKeySelectiveWithForceVersionProvider.class, method = "dynamicSQL")
    int updateByPrimaryKeySelectiveWithForceVersion(@Param("record") T record, @Param("forceVersion") boolean forceVersion);
}
