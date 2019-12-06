package com.merce.woven.common.entity.preprocess.preprocessor;

import java.lang.annotation.Annotation;

import com.merce.woven.common.entity.preprocess.PreEntity;
import com.merce.woven.common.entity.preprocess.annotation.PreUpdate;

/**
 * 继承<code>PreEntity</code>类的实体对象更新前预处理器
 *
 * @author jingwei.yang
 * @date 2019-05-30 09:52
 */
public class PreEntityUpdatePreprocessor extends AbstractPreEntityPreprocessor {

	@Override
	public Object process(Object obj) {
		if (obj == null) {
			return null;
		}
		PreEntity o = (PreEntity) obj;
		o.preUpdate();
		return o;
	}

	@Override
	public Class<? extends Annotation> annotationClass() {
		return PreUpdate.class;
	}
}
