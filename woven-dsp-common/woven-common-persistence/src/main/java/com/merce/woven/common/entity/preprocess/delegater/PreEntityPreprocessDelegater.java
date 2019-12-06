package com.merce.woven.common.entity.preprocess.delegater;

import java.lang.annotation.Annotation;

import com.merce.woven.common.entity.preprocess.preprocessor.PreEntityPreprocessor;

/**
 * 实体对象预处理器适配器定义
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午3:41:40
 */
public interface PreEntityPreprocessDelegater {

	/**
	 * 预处理方法
	 *
	 * @param annotation
	 *            注解实例
	 * @param obj
	 *            预处理对象
	 */
	void preprocess(Annotation annotation, Object obj);

	/**
	 * 添加预处理器数组
	 *
	 * @param processors
	 *            预处理器数组
	 * @return 适配器
	 */
	PreEntityPreprocessDelegater add(PreEntityPreprocessor... processors);

	/**
	 * 获取适配的预处理器
	 *
	 * @param annotation
	 *            注解实例
	 * @param clazz
	 *            处理对象实例的类型
	 * @return 适配的预处理器
	 */
	PreEntityPreprocessor get(Annotation annotation, Class<?> clazz);

}
