package com.info.baymax.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 自定义配置读取工厂类，兼容properties,yaml格式
 *
 * @author jingwei.yang
 * @date 2021年4月16日 上午10:20:41
 */
@Slf4j
public class MixPropertySourceFactory extends DefaultPropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		String sourceName = name != null ? name : resource.getResource().getFilename();
		if (sourceName.endsWith(".yml") || sourceName.endsWith(".yaml")) {
			try {
				Properties propertiesFromYaml = loadYml(resource);
				return new PropertiesPropertySource(sourceName, propertiesFromYaml);
			} catch (Exception e) {
				// for ignoreResourceNotFound
				Throwable cause = e.getCause();
				if (cause instanceof FileNotFoundException) {
					// throw (FileNotFoundException) e.getCause();
					log.warn("class path resource [" + sourceName + "] cannot be opened because it does not exist");
				} else {
					throw e;
				}
			}
		} else {
			return super.createPropertySource(name, resource);
		}
		return new PropertiesPropertySource(sourceName, new Properties());
	}

	private Properties loadYml(EncodedResource resource) throws IOException {
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}
}
