package com.info.baymax.common.entity.preprocess.preprocessor;

import com.info.baymax.common.entity.preprocess.PreEntity;

import java.lang.annotation.Annotation;

/**
 * 继承<code>PreEntity</code>实体对象抽象预处理器
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午3:22:08
 */
public abstract class AbstractPreEntityPreprocessor implements PreEntityPreprocessor {

	@Override
	public boolean supports(Annotation annotation, Class<?> clazz) {
		return annotationClass().isInstance(annotation) && PreEntity.class.isAssignableFrom(clazz);
	}

	/**
	 * 获取注解类型
	 *
	 * @return 注解类型
	 */
	public abstract Class<? extends Annotation> annotationClass();

}
