package com.info.baymax.common.entity.preprocess.preprocessor;

import java.lang.annotation.Annotation;

import com.info.baymax.common.entity.preprocess.PreEntity;
import com.info.baymax.common.entity.preprocess.annotation.PreInsert;

/**
 * 继承<code>MainTable</code>类的实体对象插入前预处理
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午3:20:35
 */
public class PreEntityInsertPreprocessor extends AbstractPreEntityPreprocessor {

	@Override
	public Object process(Object obj) {
		if (obj == null) {
			return null;
		}
		PreEntity o = (PreEntity) obj;
		o.preInsert();
		return o;
	}

	@Override
	public Class<? extends Annotation> annotationClass() {
		return PreInsert.class;
	}
}
