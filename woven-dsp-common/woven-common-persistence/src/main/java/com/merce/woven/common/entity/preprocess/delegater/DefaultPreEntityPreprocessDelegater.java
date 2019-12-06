package com.merce.woven.common.entity.preprocess.delegater;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.merce.woven.common.entity.preprocess.preprocessor.PreEntityInsertPreprocessor;
import com.merce.woven.common.entity.preprocess.preprocessor.PreEntityPreprocessor;
import com.merce.woven.common.entity.preprocess.preprocessor.PreEntityUpdatePreprocessor;
import com.merce.woven.common.utils.ICollections;

/**
 * 实体预处理适配器
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午4:01:01
 */
public class DefaultPreEntityPreprocessDelegater implements PreEntityPreprocessDelegater/* , ApplicationContextAware */ {

	/**
	 * 可用的预处理器集合
	 */
	private List<PreEntityPreprocessor> preprocessors;

	public DefaultPreEntityPreprocessDelegater() {
		this(Arrays.asList(new PreEntityInsertPreprocessor(), new PreEntityUpdatePreprocessor()));
	}

	public DefaultPreEntityPreprocessDelegater(List<PreEntityPreprocessor> preprocessors) {
		this.preprocessors = preprocessors;
	}

	/*
	 * @Override public void setApplicationContext(ApplicationContext applicationContext) {
	 * initStrategies(applicationContext); }
	 * 
	 * protected void initStrategies(ApplicationContext context) { Map<String, PreEntityPreprocessor> beans =
	 * BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PreEntityPreprocessor.class, true, false);
	 * this.preprocessors = new ArrayList<PreEntityPreprocessor>(beans.values());
	 * AnnotationAwareOrderComparator.sort(this.preprocessors); }
	 */

	@Override
	public void preprocess(Annotation annotation, Object obj) {
		if (ICollections.hasElements(preprocessors)) {
			PreEntityPreprocessor preprocessor = null;
			Class<? extends Object> clazz = obj.getClass();
			if (Iterable.class.isAssignableFrom(clazz)) {
				Iterator<?> iterator = ((Iterable<?>) obj).iterator();
				iterator.forEachRemaining(t -> {
					handle(preprocessor, annotation, t);
				});
			} else if (clazz.isArray()) {
				for (int i = 0; i < Array.getLength(obj); i++) {
					handle(preprocessor, annotation, Array.get(obj, i));
				}
			} else {
				handle(preprocessor, annotation, obj);
			}
		}
	}

	private void handle(PreEntityPreprocessor preprocessor, Annotation annotation, Object obj) {
		if (preprocessor == null) {
			preprocessor = get(annotation, obj.getClass());
		}
		if (preprocessor != null) {
			preprocessor.process(obj);
		}
	}

	@Override
	public PreEntityPreprocessDelegater add(PreEntityPreprocessor... processors) {
		if (ICollections.hasNoElements(preprocessors)) {
			preprocessors = Lists.newArrayList();
		}
		preprocessors.addAll(Arrays.asList(processors));
		return this;
	}

	@Override
	public PreEntityPreprocessor get(Annotation annotation, Class<?> clazz) {
		for (PreEntityPreprocessor preprocessor : preprocessors) {
			if (preprocessor.supports(annotation, clazz)) {
				return preprocessor;
			}
		}
		return null;
	}

}
