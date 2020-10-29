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
package com.alibaba.csp.sentinel.dashboard.rule.memory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.client.SentinelApiClient;
import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.AbstractTypedDynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.DsType;
import com.alibaba.csp.sentinel.dashboard.rule.RuleType;
import com.alibaba.csp.sentinel.slots.block.Rule;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;

/**
 * @author Eric Zhao
 */
@Component
public class RuleApiProvider extends AbstractTypedDynamicRuleProvider {

	@Autowired
	private SentinelApiClient sentinelApiClient;
	@Autowired
	private AppManagement appManagement;

	@Override
	public DsType dsType() {
		return DsType.memory;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> String fetchFromRmote(String appName, Class<T> ruleClass, RuleType ruleType) throws Exception {
		if (StringUtil.isBlank(appName)) {
			return null;
		}
		List<MachineInfo> list = appManagement.getDetailApp(appName).getMachines().stream()
				.filter(MachineInfo::isHealthy)
				.sorted((e1, e2) -> Long.compare(e2.getLastHeartbeat(), e1.getLastHeartbeat()))
				.collect(Collectors.toList());
		if (!list.isEmpty()) {
			MachineInfo machine = list.get(0);
			List<? extends Rule> fetchRules = sentinelApiClient.fetchRules(machine.getIp(), machine.getPort(),
					ruleType.getName(), ruleType.getClazz());
			if (fetchRules != null && !fetchRules.isEmpty()) {
				return JSON.toJSONString(fetchRules);
			}
		}
		return null;
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
