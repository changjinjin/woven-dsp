/**
 * Copyright 2010-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package tk.mybatis.spring.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.spring.mapper.MapperFactoryBean;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MapperScannerRegistrar implements ImportBeanDefinitionRegistrar {
	public static final Logger LOGGER = LoggerFactory.getLogger(MapperScannerRegistrar.class);

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AnnotationAttributes mapperScanAttrs = AnnotationAttributes
			.fromMap(importingClassMetadata.getAnnotationAttributes(MapperScan.class.getName()));
		if (mapperScanAttrs != null) {
			registerBeanDefinitions(importingClassMetadata, mapperScanAttrs, registry,
				generateBaseBeanName(importingClassMetadata, 0));
		}
	}

	@SuppressWarnings("rawtypes")
	void registerBeanDefinitions(AnnotationMetadata annoMeta, AnnotationAttributes annoAttrs,
								 BeanDefinitionRegistry registry, String beanName) {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
		builder.addPropertyValue("processPropertyPlaceHolders", true);

		Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
		if (!Annotation.class.equals(annotationClass)) {
			builder.addPropertyValue("annotationClass", annotationClass);
		}

		Class<?> markerInterface = annoAttrs.getClass("markerInterface");
		if (!Class.class.equals(markerInterface)) {
			builder.addPropertyValue("markerInterface", markerInterface);
		}

		Class<? extends BeanNameGenerator> generatorClass = annoAttrs.getClass("nameGenerator");
		if (!BeanNameGenerator.class.equals(generatorClass)) {
			builder.addPropertyValue("nameGenerator", BeanUtils.instantiateClass(generatorClass));
		}

		Class<? extends MapperFactoryBean> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
		if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
			builder.addPropertyValue("mapperFactoryBeanClass", mapperFactoryBeanClass);
		}

		String sqlSessionTemplateRef = annoAttrs.getString("sqlSessionTemplateRef");
		if (StringUtils.hasText(sqlSessionTemplateRef)) {
			builder.addPropertyValue("mapperHelper", annoAttrs.getString("sqlSessionTemplateRef"));
		}

		String sqlSessionFactoryRef = annoAttrs.getString("sqlSessionFactoryRef");
		if (StringUtils.hasText(sqlSessionFactoryRef)) {
			builder.addPropertyValue("sqlSessionFactoryBeanName", annoAttrs.getString("sqlSessionFactoryRef"));
		}

		String mapperHelperRef = annoAttrs.getString("mapperHelperRef");
		if (StringUtils.hasText(mapperHelperRef)) {
			builder.addPropertyReference("mapperHelper", annoAttrs.getString("mapperHelperRef"));
		}

		List<String> basePackages = new ArrayList<>();
		basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("value")).filter(StringUtils::hasText)
			.collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getStringArray("basePackages")).filter(StringUtils::hasText)
			.collect(Collectors.toList()));

		basePackages.addAll(Arrays.stream(annoAttrs.getClassArray("basePackageClasses")).map(ClassUtils::getPackageName)
			.collect(Collectors.toList()));

		if (basePackages.isEmpty()) {
			basePackages.add(getDefaultBasePackage(annoMeta));
		}

		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));

		registry.registerBeanDefinition(beanName, builder.getBeanDefinition());

	}

	private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata, int index) {
		return importingClassMetadata.getClassName() + "#" + MapperScannerRegistrar.class.getSimpleName() + "#" + index;
	}

	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}
}
