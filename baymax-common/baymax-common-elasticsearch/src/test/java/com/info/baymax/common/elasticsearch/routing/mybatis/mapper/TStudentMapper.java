package com.info.baymax.common.elasticsearch.routing.mybatis.mapper;

import com.info.baymax.common.elasticsearch.routing.entity.TStudent;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TStudentMapper extends MyIdableMapper<TStudent> {
}
