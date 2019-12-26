package org.woven.dsp.access.platform;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.info.baymax.dsp.data.platform.entity.DataResource;

public class JsonTest {
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        String json = "{\"name\":\"T_MZ_DSR_QT_DSRJTXXB_TM_oracle_info3_13\",\"id\":\"659410637377503232\",\"is_pull\":1,\"is_push\":1,\"pull_service_type\":[\"列表拉取\"],\"push_service_type\":[\"增量\"],\"expiredTime\":\"1970-01-21T16:00:00.000Z\",\"openStatus\":1}";

        ObjectMapper mapper = objectMapper();
        DataResource readValue = mapper.readValue(json, DataResource.class);

        System.out.println(readValue);
    }

    public static ObjectMapper objectMapper() {
        ObjectMapper m = new ObjectMapper();
        m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.setSerializationInclusion(Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        m.registerModule(simpleModule);
        return m;
    }
}
