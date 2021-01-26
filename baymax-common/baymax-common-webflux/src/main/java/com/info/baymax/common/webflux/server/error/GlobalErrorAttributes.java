package com.info.baymax.common.webflux.server.error;

import com.info.baymax.common.queryapi.exception.BizException;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalErrorAttributes implements ErrorAttributes {

    private static final String ERROR_ATTRIBUTE = DefaultErrorAttributes.class.getName() + ".ERROR";

    private final boolean includeException;
    private final HttpStatusDeterminer httpStatusDeterminer;

    /**
     * Create a new {@link DefaultErrorAttributes} instance that does not include the "exception" attribute.
     */
    public GlobalErrorAttributes() {
        this(false, null);
    }

    /**
     * Create a new {@link DefaultErrorAttributes} instance.
     *
     * @param includeException whether to include the "exception" attribute
     */
    public GlobalErrorAttributes(boolean includeException, HttpStatusDeterminer httpStatusDeterminer) {
        this.includeException = includeException;
        this.httpStatusDeterminer = httpStatusDeterminer;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("path", request.path());

        ServerHttpResponse response = request.exchange().getResponse();
        Throwable error = getError(request);
        HttpStatus errorStatus = determineHttpStatus(response, error);
        errorAttributes.put("status", errorStatus.value());
        errorAttributes.put("error", errorStatus.getReasonPhrase());
        errorAttributes.put("message", determineMessage(error));
        handleException(errorAttributes, determineException(error), includeStackTrace);
        return errorAttributes;
    }

    private HttpStatus determineHttpStatus(ServerHttpResponse response, Throwable error) {
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getStatus();
        }

        if (response != null && response.getStatusCode() != null && !response.getStatusCode().is2xxSuccessful()) {
            return response.getStatusCode();
        }

        if (httpStatusDeterminer != null) {
            HttpStatus determineHttpStatus = httpStatusDeterminer.determineHttpStatus(response, error);
            if (determineHttpStatus != null) {
                return determineHttpStatus;
            }
        }
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(),
            ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.code();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable error) {
        if (error instanceof WebExchangeBindException) {
            return error.getMessage();
        }
        if (error instanceof ResponseStatusException) {
            return ((ResponseStatusException) error).getReason();
        }

        // 认证异常和业务异常消息
        if (/* error instanceof AuthenticationException || */error instanceof BizException) {
            return error.getMessage();
        }
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(),
            ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.reason();
        }
        return error.getMessage();
    }

    private Throwable determineException(Throwable error) {
        if (error instanceof ResponseStatusException) {
            return (error.getCause() != null) ? error.getCause() : error;
        }
        return error;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void handleException(Map<String, Object> errorAttributes, Throwable error, boolean includeStackTrace) {
        if (this.includeException) {
            errorAttributes.put("exception", error.getClass().getName());
        }
        if (includeStackTrace) {
            addStackTrace(errorAttributes, error);
        }

        if (error instanceof BindingResult) {
            BindingResult result = (BindingResult) error;
            if (result.hasErrors()) {
                errorAttributes.put("errors", result.getAllErrors());
            }
        }
    }

    @Override
    public Throwable getError(ServerRequest request) {
        return (Throwable) request.attribute(ERROR_ATTRIBUTE)
            .orElseThrow(() -> new IllegalStateException("Missing exception attribute in ServerWebExchange"));
    }

    @Override
    public void storeErrorInformation(Throwable error, ServerWebExchange exchange) {
        exchange.getAttributes().putIfAbsent(ERROR_ATTRIBUTE, error);
    }

}
