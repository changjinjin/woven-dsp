package com.info.baymax.dsp.data.sys.initialize;

import java.io.FileNotFoundException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.springframework.util.ResourceUtils;

import com.info.baymax.common.utils.JaxbUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PermsParser {

	// 就从配置文件中加载系统权限列表
	public static URL loadPermsFromXml(String xmlFilePath) {
		try {
			return ResourceUtils.getURL(xmlFilePath);
		} catch (FileNotFoundException e) {
			log.warn("权限初始化文件不存在，使用系统默认权限数据", e);
			return null;
		}
	}

	public static PermRoots getInitPerms(InitConfig initConfig) throws JAXBException {
		// 加载权限列表
		URL url = loadPermsFromXml(initConfig.getPermsFile());
		if (url == null) {
			url = loadPermsFromXml("classpath:init/perms/all.xml");
		}
		return JaxbUtils.xml2java(url, PermRoots.class);
	}
}
