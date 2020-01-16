package com.info.baymax.dsp.data.dataset.service.core.impl;

import com.info.baymax.common.mybatis.mapper.MyIdableMapper;
import com.info.baymax.common.service.entity.EntityClassServiceImpl;
import com.info.baymax.dsp.data.dataset.entity.core.ProjectEntity;
import com.info.baymax.dsp.data.dataset.mybatis.mapper.core.ProjectEntityMapper;
import com.info.baymax.dsp.data.dataset.service.core.ProjectEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectEntityServiceImpl extends EntityClassServiceImpl<ProjectEntity> implements ProjectEntityService {
	@Autowired
	private ProjectEntityMapper projectEntityMapper;

	@Override
	public MyIdableMapper<ProjectEntity> getMyIdableMapper() {
		return projectEntityMapper;
	}

}
