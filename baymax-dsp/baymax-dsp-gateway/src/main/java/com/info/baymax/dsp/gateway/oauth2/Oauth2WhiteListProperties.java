package com.info.baymax.dsp.gateway.oauth2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.info.baymax.common.utils.ICollections;
import com.info.baymax.common.utils.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.token.ignored")
@Slf4j
public class Oauth2WhiteListProperties {

    /**
     * 忽略的静态资源请求
     */
    private String[] staticResources;

    /**
     * 所有方法都忽略的请求
     */
    private String[] anyMethods;

    /**
     * 前端路由白名单
     */
    private String[] frontRoutings;

    /**
     * 前端路由白名单配置文件
     */
    private String frontRoutingsFile;

    @PostConstruct
    public void init() {
        if (frontRoutings == null) {
            frontRoutings = new String[0];
        }
        HashSet<String> routingSet = Sets.newHashSet(frontRoutings);
        routingSet.addAll(loadWhiteList());
        frontRoutings = routingSet.stream().toArray(String[]::new);
    }

    @SuppressWarnings("unchecked")
    public List<String> loadWhiteList() {
        try {
            frontRoutingsFile = StringUtils.defaultIfEmpty(frontRoutingsFile, "classpath:webapp/serverconfig.json");
            URL url = ResourceUtils.getURL(frontRoutingsFile);
            if (url != null) {
                Map<String, Object> fromJson = JsonUtils.fromJson(url, new TypeReference<Map<String, Object>>() {
                });
                if (fromJson != null) {
                    List<String> whiteList = (List<String>) fromJson.get("whiteList");
                    if (ICollections.hasElements(whiteList)) {
                        return whiteList;
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Init front end routings whiteList failed:{}", e.getMessage());
        }
        return Lists.newArrayList("/");
    }

    public String[] getAllWhiteList() {
        String[] addAll = ArrayUtils.addAll(staticResources, anyMethods);
        return ArrayUtils.addAll(addAll, frontRoutings);
    }
}
