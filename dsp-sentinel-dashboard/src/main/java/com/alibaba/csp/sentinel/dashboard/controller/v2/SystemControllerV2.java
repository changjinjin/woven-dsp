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
package com.alibaba.csp.sentinel.dashboard.controller.v2;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.dashboard.auth.AuthAction;
import com.alibaba.csp.sentinel.dashboard.auth.AuthService.PrivilegeType;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.SystemRuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.repository.rule.RuleRepository;
import com.alibaba.csp.sentinel.dashboard.rule.RuleType;
import com.alibaba.csp.sentinel.util.StringUtil;

/**
 * @author leyou(lihao)
 */
@RestController
@RequestMapping("/v2/system")
public class SystemControllerV2 extends RuleController<SystemRuleEntity> {
    private final Logger logger = LoggerFactory.getLogger(SystemControllerV2.class);

    @Autowired
    private RuleRepository<SystemRuleEntity, Long> repository;

    private <R> Result<R> checkBasicParams(String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        return null;
    }

    @GetMapping("/rules")
    @AuthAction(PrivilegeType.READ_RULE)
    public Result<List<SystemRuleEntity>> apiQueryMachineRules(@RequestParam String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        try {
            List<SystemRuleEntity> rules = ruleProvider.getRules(app, SystemRuleEntity.class, RuleType.system);
            rules = repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            logger.error("Query machine system rules error", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    private int countNotNullAndNotNegative(Number... values) {
        int notNullCount = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && values[i].doubleValue() >= 0) {
                notNullCount++;
            }
        }
        return notNullCount;
    }

    @PostMapping("/rule")
    @AuthAction(value = PrivilegeType.WRITE_RULE)
    public Result<SystemRuleEntity> apiAdd(@RequestBody SystemRuleEntity entity) {
        Result<SystemRuleEntity> checkResult = checkBasicParams(entity.getApp());
        if (checkResult != null) {
            return checkResult;
        }

        int notNullCount = countNotNullAndNotNegative(entity.getHighestSystemLoad(), entity.getAvgRt(),
            entity.getMaxThread(), entity.getQps(), entity.getHighestCpuUsage());
        if (notNullCount != 1) {
            return Result.ofFail(-1, "only one of [highestSystemLoad, avgRt, maxThread, qps,highestCpuUsage] "
                + "value must be set > 0, but " + notNullCount + " values get");
        }
        if (null != entity.getHighestCpuUsage() && entity.getHighestCpuUsage() > 1) {
            return Result.ofFail(-1, "highestCpuUsage must between [0.0, 1.0]");
        }
        Date date = new Date();
        entity.setGmtCreate(date);
        entity.setGmtModified(date);
        try {
            entity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("Add SystemRule error", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(entity.getApp(), RuleType.system)) {
            logger.warn("Publish system rules fail after rule add");
        }
        return Result.ofSuccess(entity);
    }

    @PutMapping("/rule/{id}")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<SystemRuleEntity> apiUpdateIfNotNull(@PathVariable("id") Long id,
                                                       @RequestBody SystemRuleEntity entity) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }
        SystemRuleEntity exEntity = repository.findById(id);
        if (exEntity == null) {
            return Result.ofFail(-1, "id " + id + " dose not exist");
        }

        if (StringUtil.isNotBlank(entity.getApp())) {
            exEntity.setApp(entity.getApp().trim());
        }
        if (entity.getHighestSystemLoad() != null) {
            if (entity.getHighestSystemLoad() < 0) {
                return Result.ofFail(-1, "highestSystemLoad must >= 0");
            }
            exEntity.setHighestSystemLoad(entity.getHighestSystemLoad());
        }
        if (entity.getHighestCpuUsage() != null) {
            if (entity.getHighestCpuUsage() < 0) {
                return Result.ofFail(-1, "highestCpuUsage must >= 0");
            }
            if (entity.getHighestCpuUsage() > 1) {
                return Result.ofFail(-1, "highestCpuUsage must <= 1");
            }
            exEntity.setHighestCpuUsage(entity.getHighestCpuUsage());
        }
        if (entity.getAvgRt() != null) {
            if (entity.getAvgRt() < 0) {
                return Result.ofFail(-1, "avgRt must >= 0");
            }
            exEntity.setAvgRt(entity.getAvgRt());
        }
        if (entity.getMaxThread() != null) {
            if (entity.getMaxThread() < 0) {
                return Result.ofFail(-1, "maxThread must >= 0");
            }
            exEntity.setMaxThread(entity.getMaxThread());
        }
        if (entity.getQps() != null) {
            if (entity.getQps() < 0) {
                return Result.ofFail(-1, "qps must >= 0");
            }
            exEntity.setQps(entity.getQps());
        }
        Date date = new Date();
        exEntity.setGmtModified(date);
        try {
            exEntity = repository.save(entity);
        } catch (Throwable throwable) {
            logger.error("save error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(exEntity.getApp(), RuleType.system)) {
            logger.info("publish system rules fail after rule update");
        }
        return Result.ofSuccess(exEntity);
    }

    @DeleteMapping("/rule/{id}")
    @AuthAction(PrivilegeType.DELETE_RULE)
    public Result<?> delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }
        SystemRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }
        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            logger.error("delete error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(oldEntity.getApp(), RuleType.system)) {
            logger.info("publish system rules fail after rule delete");
        }
        return Result.ofSuccess(id);
    }
}
