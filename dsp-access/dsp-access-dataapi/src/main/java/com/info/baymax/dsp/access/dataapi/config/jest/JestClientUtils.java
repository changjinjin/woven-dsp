package com.info.baymax.dsp.access.dataapi.config.jest;

import com.google.gson.Gson;
import com.info.baymax.dsp.access.dataapi.config.jest.JestConf.Proxy;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.http.HttpHost;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Map;

public class JestClientUtils {

    public static JestClient jestClient(Map<String, String> conf) {
        return jestClient(JestConf.fromMap(conf), defaultGson());
    }

    public static JestClient jestClient(JestConf conf) {
        return jestClient(conf, defaultGson());
    }

    public static JestClient jestClient(JestConf conf, ObjectProvider<Gson> gson) {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(createHttpClientConfig(conf, gson));
        return factory.getObject();
    }

    private static HttpClientConfig createHttpClientConfig(JestConf conf, ObjectProvider<Gson> gson) {
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(conf.getUris());
        PropertyMapper map = PropertyMapper.get();
        map.from(conf::getUsername).whenHasText()
            .to((username) -> builder.defaultCredentials(username, conf.getPassword()));
        Proxy proxy = conf.getProxy();
        map.from(proxy::getHost).whenHasText().to((host) -> {
            Assert.notNull(proxy.getPort(), "Proxy port must not be null");
            builder.proxy(new HttpHost(host, proxy.getPort()));
        });
        map.from(gson::getIfUnique).whenNonNull().to(builder::gson);
        map.from(conf::isMultiThreaded).to(builder::multiThreaded);
        map.from(conf::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis).to(builder::connTimeout);
        map.from(conf::getReadTimeout).whenNonNull().asInt(Duration::toMillis).to(builder::readTimeout);
        return builder.build();
    }

    private static ObjectProvider<Gson> defaultGson() {
        return new ObjectProvider<Gson>() {
            @Override
            public Gson getObject() throws BeansException {
                return null;
            }

            @Override
            public Gson getObject(Object... args) throws BeansException {
                return null;
            }

            @Override
            public Gson getIfAvailable() throws BeansException {
                return null;
            }

            @Override
            public Gson getIfUnique() throws BeansException {
                return null;
            }
        };
    }

}
