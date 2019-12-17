package com.info.baymax.dsp.data.dataset.mybatis.mapper.core;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.dsp.data.dataset.entity.core.Dataset;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DatasetMapper extends MyIdableMapper<Dataset> {
}
