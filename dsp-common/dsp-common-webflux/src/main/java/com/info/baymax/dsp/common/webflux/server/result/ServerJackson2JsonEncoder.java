package com.info.baymax.dsp.common.webflux.server.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;

public class ServerJackson2JsonEncoder extends Jackson2JsonEncoder {

    private static final MediaType[] SUPPORTED_TYPES = {MediaType.APPLICATION_JSON,
        new MediaType("application", "*+json")};

    public ServerJackson2JsonEncoder() {
        super();
    }

    public ServerJackson2JsonEncoder(ObjectMapper mapper) {
        super(mapper, SUPPORTED_TYPES);
    }

}
