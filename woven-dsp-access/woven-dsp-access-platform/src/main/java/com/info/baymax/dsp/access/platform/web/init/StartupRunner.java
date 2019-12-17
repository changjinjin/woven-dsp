package com.info.baymax.dsp.access.platform.web.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.info.baymax.common.comp.cache.service.CacheService;
import com.info.baymax.dsp.data.sys.initialize.InitConfig;
import com.info.baymax.dsp.data.sys.initialize.TenantInitializer;

import lombok.extern.slf4j.Slf4j;

/**
 * 服务启动执行初始化任务
 *
 * @author jingwei.yang
 * @date 2019年5月27日 上午9:52:33
 */
@Slf4j
@Component
@Order(value = 1)
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private InitConfig initConfig;
//	@Autowired
//	private CacheService cacheService;

    @Autowired
    private TenantInitializer tenantInitializer;

    @Override
    public void run(String... args) throws Exception {
        log.info(">> ### 服务启动执行，系统初始化选项执行开始 ### <<");
        cleanCache();
        initTenants();
        log.info(">> ### 服务启动执行，系统初始化选项执行结束 ### <<");
    }

    private void cleanCache() {
        log.info("--> 清理系统缓存开始.... ");
//		cacheService.clear();
        log.info("--> 清理系统缓存结束.... ");
    }

    private void initTenants() {
        log.info("--> 初始化租户信息开始.... ");
        tenantInitializer.initAllTenantAdminAndPerms(initConfig);
        log.info("--> 初始化租户信息结束.... ");
    }
}