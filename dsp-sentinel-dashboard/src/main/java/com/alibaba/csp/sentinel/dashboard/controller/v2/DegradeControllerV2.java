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
import org.springframework.http.MediaType;
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
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.domain.Result;
import com.alibaba.csp.sentinel.dashboard.rule.RuleType;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.util.StringUtil;

/**
 * @author leyou
 */
@RestController
@RequestMapping(value = "/v2/degrade", produces = MediaType.APPLICATION_JSON_VALUE)
public class DegradeControllerV2 extends RuleController<DegradeRuleEntity> {
    private final Logger logger = LoggerFactory.getLogger(DegradeControllerV2.class);

    @GetMapping("/rules")
    @AuthAction(PrivilegeType.READ_RULE)
    public Result<List<DegradeRuleEntity>> queryMachineRules(@RequestParam String app) {
        if (StringUtil.isEmpty(app)) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        try {
            List<DegradeRuleEntity> rules = ruleProvider.getRules(app, DegradeRuleEntity.class, RuleType.degrade);
            rules = repository.saveAll(rules);
            return Result.ofSuccess(rules);
        } catch (Throwable throwable) {
            logger.error("queryApps error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
    }

    @PostMapping("/rule")
    @AuthAction(value = PrivilegeType.WRITE_RULE)
    public Result<DegradeRuleEntity> add(@RequestBody DegradeRuleEntity entity) {
        if (StringUtil.isBlank(entity.getApp())) {
            return Result.ofFail(-1, "app can't be null or empty");
        }
        // if (StringUtil.isBlank(entity.getIp())) {
        // return Result.ofFail(-1, "ip can't be null or empty");
        // }
        // if (entity.getPort() == null) {
        // return Result.ofFail(-1, "port can't be null");
        // }
        if (StringUtil.isBlank(entity.getLimitApp())) {
            return Result.ofFail(-1, "limitApp can't be null or empty");
        }
        if (StringUtil.isBlank(entity.getResource())) {
            return Result.ofFail(-1, "resource can't be null or empty");
        }
        if (entity.getCount() == null) {
            return Result.ofFail(-1, "count can't be null");
        }
        if (entity.getTimeWindow() == null) {
            return Result.ofFail(-1, "timeWindow can't be null");
        }
        if (entity.getGrade() == null) {
            return Result.ofFail(-1, "grade can't be null");
        }
        if (entity.getGrade() < RuleConstant.DEGRADE_GRADE_RT
            || entity.getGrade() > RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT) {
            return Result.ofFail(-1, "Invalid grade: " + entity.getGrade());
        }
        DegradeRuleEntity exEntity = new DegradeRuleEntity();
        exEntity.setApp(entity.getApp().trim());
        exEntity.setIp(entity.getIp().trim());
        exEntity.setPort(entity.getPort());
        exEntity.setLimitApp(entity.getLimitApp().trim());
        exEntity.setResource(entity.getResource().trim());
        exEntity.setCount(entity.getCount());
        exEntity.setTimeWindow(entity.getTimeWindow());
        exEntity.setGrade(entity.getGrade());
        Date date = new Date();
        exEntity.setGmtCreate(date);
        exEntity.setGmtModified(date);
        try {
            exEntity = repository.save(exEntity);
        } catch (Throwable throwable) {
            logger.error("add error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(entity.getApp(), RuleType.degrade)) {
            logger.info("publish degrade rules fail after rule add");
        }
        return Result.ofSuccess(entity);
    }

    @PutMapping("/rule/{id}")
    @AuthAction(PrivilegeType.WRITE_RULE)
    public Result<DegradeRuleEntity> updateIfNotNull(@PathVariable("id") Long id,
                                                     @RequestBody DegradeRuleEntity entity) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }
        if (entity.getGrade() != null) {
            if (entity.getGrade() < RuleConstant.DEGRADE_GRADE_RT
                || entity.getGrade() > RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT) {
                return Result.ofFail(-1, "Invalid grade: " + entity.getGrade());
            }
        }
        DegradeRuleEntity exEntity = repository.findById(id);
        if (exEntity == null) {
            return Result.ofFail(-1, "id " + id + " dose not exist");
        }

        if (StringUtil.isNotBlank(entity.getApp())) {
            entity.setApp(entity.getApp().trim());
        }

        if (StringUtil.isNotBlank(entity.getLimitApp())) {
            exEntity.setLimitApp(entity.getLimitApp().trim());
        }
        if (StringUtil.isNotBlank(entity.getResource())) {
            exEntity.setResource(entity.getResource().trim());
        }
        if (entity.getCount() != null) {
            exEntity.setCount(entity.getCount());
        }
        if (entity.getTimeWindow() != null) {
            exEntity.setTimeWindow(entity.getTimeWindow());
        }
        if (entity.getGrade() != null) {
            exEntity.setGrade(entity.getGrade());
        }
        Date date = new Date();
        exEntity.setGmtModified(date);
        try {
            exEntity = repository.save(exEntity);
        } catch (Throwable throwable) {
            logger.error("save error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(entity.getApp(), RuleType.degrade)) {
            logger.info("publish degrade rules fail after rule update");
        }
        return Result.ofSuccess(entity);
    }

    @DeleteMapping("/rule/{id}")
    @AuthAction(PrivilegeType.DELETE_RULE)
    public Result<Long> delete(@PathVariable("id") Long id) {
        if (id == null) {
            return Result.ofFail(-1, "id can't be null");
        }

        DegradeRuleEntity oldEntity = repository.findById(id);
        if (oldEntity == null) {
            return Result.ofSuccess(null);
        }

        try {
            repository.delete(id);
        } catch (Throwable throwable) {
            logger.error("delete error:", throwable);
            return Result.ofThrowable(-1, throwable);
        }
        if (!publishRules(oldEntity.getApp(), RuleType.degrade)) {
            logger.info("publish degrade rules fail after rule delete");
        }
        return Result.ofSuccess(id);
    }
}
