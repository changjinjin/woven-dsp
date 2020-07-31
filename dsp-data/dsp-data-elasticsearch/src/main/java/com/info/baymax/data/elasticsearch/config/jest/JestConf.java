package com.info.baymax.data.elasticsearch.config.jest;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
public class JestConf implements Serializable {
    private static final long serialVersionUID = 660896701924731075L;
    /**
     * Comma-separated list of the Elasticsearch instances to use.
     */
    @Builder.Default
    private List<String> uris = new ArrayList<>(Collections.singletonList("http://localhost:9200"));

    /**
     * Login username.
     */
    private String username;

    /**
     * Login password.
     */
    private String password;

    /**
     * Whether to enable connection requests from multiple execution threads.
     */
    @Builder.Default
    private boolean multiThreaded = true;

    /**
     * Connection timeout.
     */
    @Builder.Default
    private Duration connectionTimeout = Duration.ofSeconds(30);

    /**
     * Read timeout.
     */
    @Builder.Default
    private Duration readTimeout = Duration.ofSeconds(30);

    /**
     * Proxy settings.
     */
    @Builder.Default
    private final Proxy proxy = Proxy.builder().build();

    @Setter
    @Getter
    @Builder
    public static class Proxy {
        /**
         * Proxy host the HTTP client should use.
         */
        private String host;

        /**
         * Proxy port the HTTP client should use.
         */
        private Integer port;
    }

    public static JestConf from(Map<String, String> conf) {
        JestConfBuilder builder = JestConf.builder();
        builder.uris(formatUris(Arrays.asList(conf.getOrDefault("ipAddresses", "").split(","))));

        String username = conf.get("username");
        if (StringUtils.isNotEmpty(username)) {
            builder.username(username);
        }

        String password = conf.get("password");
        if (StringUtils.isNotEmpty(password)) {
            builder.password(password);
        }

        String multiThreaded = conf.get("multiThreaded");
        if (StringUtils.isNotEmpty(multiThreaded)) {
            builder.multiThreaded(Boolean.valueOf(multiThreaded));
        }

        String connectionTimeout = conf.get("connectionTimeout");
        if (StringUtils.isNotEmpty(connectionTimeout)) {
            builder.connectionTimeout(Duration.ofSeconds(Long.valueOf(connectionTimeout)));
        }

        String readTimeout = conf.get("readTimeout");
        if (StringUtils.isNotEmpty(readTimeout)) {
            builder.readTimeout(Duration.ofSeconds(Long.valueOf(readTimeout)));
        }

        String proxyHost = conf.get("proxy.host");
        String proxyPort = conf.get("proxy.port");
        if (StringUtils.isNotEmpty(proxyHost)) {
            builder.proxy(Proxy.builder().host(proxyHost)
                .port(Integer.valueOf(StringUtils.defaultIfBlank(proxyPort, "80"))).build());
        }
        return builder.build();
    }

    private static List<String> formatUris(List<String> urls) {
        return urls.stream().map(t -> t.startsWith("http://") ? t : ("http://" + t)).collect(Collectors.toList());
    }
}
