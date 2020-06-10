package com.info.baymax.dsp.data.sys.initialize;

import java.io.FileNotFoundException;
import java.net.URL;

import org.springframework.util.ResourceUtils;

import com.info.baymax.common.utils.JaxbUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PermsParser {

    // 就从配置文件中加载系统权限列表
    public URL loadPermsFromXml(String xmlFilePath) {
        try {
            return ResourceUtils.getURL(xmlFilePath);
        } catch (FileNotFoundException e) {
            log.warn("Permission initialization file does not exist, use system default permission data.", e);
            return null;
        }
    }

    public PermRoots getInitPerms(String xmlFilePath) {
        // 加载权限列表
        URL url = loadPermsFromXml(xmlFilePath);
        if (url == null) {
            url = loadPermsFromXml("classpath:init/perms/all.xml");
        }
        try {
            return JaxbUtils.xml2java(url, PermRoots.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
