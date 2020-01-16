package com.info.baymax.dsp.data.platform.mybatis.mapper;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.platform.entity.DataPolicy;
import com.info.baymax.dsp.data.platform.entity.DataService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DataPolicyMapper extends MyIdableMapper<DataPolicy> {

}
