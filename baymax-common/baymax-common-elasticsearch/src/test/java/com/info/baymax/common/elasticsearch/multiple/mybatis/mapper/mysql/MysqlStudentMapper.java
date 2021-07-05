package com.info.baymax.common.elasticsearch.multiple.mybatis.mapper.mysql;

import com.info.baymax.common.elasticsearch.multiple.entity.mysql.MysqlStudent;
import com.info.baymax.common.persistence.mybatis.mapper.MyIdableMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MysqlStudentMapper extends MyIdableMapper<MysqlStudent> {
}
