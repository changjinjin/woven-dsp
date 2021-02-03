package com.info.baymax.dsp.gateway.metrics;

import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.dsp.gateway.web.method.RequestUriMappingsHolder;
import io.micrometer.core.instrument.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.metrics.web.reactive.server.WebFluxTagsProvider;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by pengchuan.chen on 2019/6/19
 */
public class DefaultWebFluxTagsProvider implements WebFluxTagsProvider {

    private static final String INGORE_REGEX = ".+(.jpeg|.jpg|.png|.gif|.icon|.ico|.css|.map|.js|.html|.jsp|.json|.xml|.txt|.md|.csv|.pdf|.xls|.xlsx|.doc|.docx|.ttf|.eot|.woff2|.sh|.py)$";

    private RequestUriMappingsHolder holder;

    public DefaultWebFluxTagsProvider(RequestUriMappingsHolder holder) {
        this.holder = holder;
    }

    @Override
    public Iterable<Tag> httpRequestTags(ServerWebExchange exchange, Throwable exception) {
        String path = exchange.getRequest().getPath().value();
        if (uriMatch(path, INGORE_REGEX)) {
            // @formatter:off
            return Arrays.asList(
                WebFluxTags.method(exchange),
                WebFluxTags.uri(exchange),
                WebFluxTags.operation(exchange, holder),
                WebFluxTags.params(exchange),
                WebFluxTags.exception(exception),
                WebFluxTags.status(exchange),
                WebFluxTags.outcome(exchange),
                WebFluxTags.clientIp(exchange),
                Tag.of("sid", UUID.randomUUID().toString()),
                Tag.of("instance", "dsp-gateway"),
                Tag.of("write_time", Long.toString(new Date().getTime())),
                Tag.of("tenantId", StringUtils.defaultString(SaasContext.getCurrentTenantId(), "default")),
                Tag.of("tenantName", StringUtils.defaultString(SaasContext.getCurrentTenantName(), "default")),
                Tag.of("userId", StringUtils.defaultString(SaasContext.getCurrentUserId(), "SYSTEM")),
                Tag.of("loginId", StringUtils.defaultString(SaasContext.getCurrentUsername(), "admin"))
            );
            // @formatter:on
        }
        return null;
    }

    private static boolean uriMatch(String path, String ingoreRegex) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        Matcher matcher = Pattern.compile(ingoreRegex, Pattern.CASE_INSENSITIVE).matcher(path);
        return !matcher.find() && !(path.lastIndexOf("/bower_components/") >= 0)
            && !(path.lastIndexOf("/api/logs/metrics/request/page") >= 0) && !(path.lastIndexOf("/actuator/") >= 0)
            && !path.startsWith("/static/");
    }
}