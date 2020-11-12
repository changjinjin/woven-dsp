package com.info.baymax.dsp.data.dataset.service.resource;

import com.info.baymax.common.entity.gene.Idable;

/**
 * 拥有resourceId属性的类继承，方便抽取通用方法
 * 
 * @author jingwei.yang
 * @date 2019年10月17日 下午2:18:06
 */
public interface ResourceId extends Idable<String>{
	String getResourceId();

	void setResourceId(String resourceId);
}
