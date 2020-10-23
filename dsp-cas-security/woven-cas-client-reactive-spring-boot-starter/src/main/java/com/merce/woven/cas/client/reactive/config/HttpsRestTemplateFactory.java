package com.merce.woven.cas.client.reactive.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("deprecation")
@Slf4j
public class HttpsRestTemplateFactory {
    public static RestTemplate getInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {
        public static RestTemplate instance;
        private static final Integer CONNECT_TIME_OUT;
        private static final Integer CONNECT_REQUEST_TIME_OUT;
        private static final Integer SOCKET_TIME_OUT;
        private static final Integer MAX_CONN_TOTAL;
        private static final Integer MAX_CONN_PER_ROUT;

        static {
            CONNECT_TIME_OUT = 5000;
            CONNECT_REQUEST_TIME_OUT = 5000;
            SOCKET_TIME_OUT = 5000;
            MAX_CONN_TOTAL = 600;
            MAX_CONN_PER_ROUT = 60;
            try {
                final SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true).build();
                final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null,
                    NoopHostnameVerifier.INSTANCE);
                final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create().register("http", new PlainConnectionSocketFactory())
                    .register("https", socketFactory).build();
                final PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(
                    socketFactoryRegistry);
                phccm.setMaxTotal(InstanceHolder.MAX_CONN_TOTAL);
                phccm.setDefaultMaxPerRoute(InstanceHolder.MAX_CONN_PER_ROUT);
                final CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setConnectionManager(phccm).setConnectionManagerShared(true).build();
                final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                    httpClient);
                factory.setConnectionRequestTimeout(InstanceHolder.CONNECT_REQUEST_TIME_OUT);
                factory.setConnectTimeout(InstanceHolder.CONNECT_TIME_OUT);
                factory.setReadTimeout(InstanceHolder.SOCKET_TIME_OUT);
                InstanceHolder.instance = new RestTemplate(factory);
                InstanceHolder.instance.getMessageConverters().set(1,
                    new StringHttpMessageConverter(StandardCharsets.UTF_8));
            } catch (Exception e) {
                log.error("初始化RestTemplate出现异常:", (Throwable) e);
            }
        }
    }
}
