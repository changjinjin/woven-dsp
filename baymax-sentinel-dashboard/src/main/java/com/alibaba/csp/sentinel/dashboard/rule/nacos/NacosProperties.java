/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = NacosProperties.PREFIX)
public class NacosProperties {
    public static final String PREFIX = "sentinel.datasource.nacos";

    private String serverAddr;

    private Properties properties;

    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        if (!properties.containsKey("serverAddr")) {
            if (StringUtils.isNoneEmpty(serverAddr)) {
                properties.put("serverAddr", getServerAddr());
            } else {
                properties.put("serverAddr", "localhost:8848");
            }
        }
        return properties;
    }

}
