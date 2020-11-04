package com.info.baymax.common.swagger.handle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = SwaggerHandleProperties.PREFIX)
public class SwaggerHandleProperties {
    public static final String PREFIX = "swagger2.handler";

    private boolean autoStartup = true;
    private boolean autoExecute = true;

}
