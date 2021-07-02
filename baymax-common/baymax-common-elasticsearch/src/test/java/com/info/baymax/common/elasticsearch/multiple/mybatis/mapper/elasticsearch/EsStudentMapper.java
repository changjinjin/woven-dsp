package com.info.baymax.common.elasticsearch.multiple.mybatis.mapper.elasticsearch;

import com.info.baymax.common.elasticsearch.multiple.entity.elasticsearch.EsStudent;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EsStudentMapper extends MyIdableMapper<EsStudent> {
}
