package com.info.baymax.dsp.common.webflux.server.result;

import com.info.baymax.common.comp.config.JacksonConfig;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

public class ServerJackson2JsonEncoder extends Jackson2JsonEncoder {
    public ServerJackson2JsonEncoder() {
        JacksonConfig.config(getObjectMapper());
    }
}
