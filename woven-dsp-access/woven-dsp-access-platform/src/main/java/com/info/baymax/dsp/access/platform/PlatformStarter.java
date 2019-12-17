package com.info.baymax.dsp.access.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;

@SpringCloudApplication
//@EnableUserInfoTransmitter
@EnableFeignClients(basePackages = {"com.info.baymax.dsp.access.platform"})
@ComponentScan(basePackages = {"com.info.baymax"})
@EntityScan(basePackages = {"com.info.baymax.dsp.data.**.entity", "com.merce.woven.data"})
@MapperScan(basePackages = {"com.info.baymax.dsp.data.**.mapper", "com.merce.woven.data.mybatis.mapper"})
public class PlatformStarter {

    /**
     * 使用统配的方式加载配置文件，已适配不同的profile.
     * 一定一定一定要注意，这里的PropertySourcesPlaceholderConfigurer获取方法是静态的，如果不是静态的则会失败。
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer getPropertyPlaceholderConfigurer() throws IOException {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setLocations(
            new PathMatchingResourcePatternResolver().getResources("classpath*:**/access-platform*.properties"));
        return ppc;
    }

    public static void main(String[] args) {
        SpringApplication.run(PlatformStarter.class, args);
    }
}