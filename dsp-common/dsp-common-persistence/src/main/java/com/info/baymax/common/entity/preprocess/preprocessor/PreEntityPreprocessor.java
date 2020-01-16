package com.info.baymax.common.entity.preprocess.preprocessor;

import java.lang.annotation.Annotation;

/**
 * 实体对象预处理器
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午3:19:20
 */
public interface PreEntityPreprocessor {

	/**
	 * 处理一个预处理实体的实例
	 *
	 * @param obj
	 *            预处理对象
	 * @return 预处理之后的对象
	 */
	Object process(Object obj);

	/**
	 * 是否适配
	 *
	 * @param annotation
	 *            注解对象
	 * @param clazz
	 *            实体类型
	 * @return 是否适配
	 */
	boolean supports(Annotation annotation, Class<?> clazz);
}
