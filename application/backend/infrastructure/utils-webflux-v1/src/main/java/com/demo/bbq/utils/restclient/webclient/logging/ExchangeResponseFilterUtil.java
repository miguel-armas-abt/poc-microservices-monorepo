package com.demo.bbq.utils.restclient.webclient.logging;

import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_REQ_METHOD;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.TRACKING_INFO;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_REQ_URI;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_RES_HEADERS;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_RES_BODY;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_RES_STATUS;

import com.demo.bbq.utils.restclient.webclient.logging.mdc.MDCUtil;
import com.demo.bbq.utils.restclient.webclient.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.restclient.webclient.properties.LoggingBaseProperties;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
public class ExchangeResponseFilterUtil {

    public static Context captureRequestDetails(ClientRequest request) {
        var httpHeaders = request.headers();
        return Context.of(
            TRACKING_INFO, httpHeaders
        );
    }

    public static Mono<ClientResponse> handleResponse(LoggingBaseProperties loggingProperties,
                                                      List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                                      ClientRequest clientRequest,
                                                      ClientResponse clientResponse,
                                                      Context context) {

        Mono<String> responseBodyMono = clientResponse.bodyToMono(String.class)
            .defaultIfEmpty(StringUtils.EMPTY);
        return responseBodyMono.flatMap(responseBody -> {
            createLogs(loggingProperties, headerObfuscationStrategies, clientRequest, clientResponse, responseBody, context);
            var headers = clientResponse.headers().asHttpHeaders();
            return Mono.just(ClientResponse.create(clientResponse.statusCode())
                .headers(h -> h.addAll(headers))
                .body(responseBody)
                .build());
        });
    }

    private static void createLogs(LoggingBaseProperties loggingProperties,
                                   List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                   ClientRequest clientRequest,
                                   ClientResponse response,
                                   String responseBody,
                                   Context context) {
        try {
            populateMDCFromRequestContext(loggingProperties, context);
            putStatusCode(response);
            ThreadContext.put(MDC_REQ_METHOD, clientRequest.method().toString());
            ThreadContext.put(MDC_REQ_URI, clientRequest.url().toString());
            ThreadContext.put(MDC_RES_HEADERS, HeaderObfuscatorUtil.process(loggingProperties, headerObfuscationStrategies, response.headers().asHttpHeaders()));
            ThreadContext.put(MDC_RES_BODY, BodyObfuscatorUtil.process(loggingProperties, responseBody));
            log.info("HTTP Response - Successful method execution");

        } catch (Exception ex) {
            log.error("Error logWebClientResponse, Exception class: " + ex.getClass(), ex);
        }
        ThreadContext.clearAll();
    }

    private static void putStatusCode(ClientResponse response){
        try{
            HttpStatusCode statusCode = response.statusCode();
            ThreadContext.put(MDC_RES_STATUS, statusCode.toString());
        }catch (IllegalArgumentException ex){
            MDC.put(MDC_RES_STATUS, String.valueOf(response.statusCode()));
        }
    }

    private static void populateMDCFromRequestContext(LoggingBaseProperties loggingProperties, Context context) {
        context.getOrEmpty(TRACKING_INFO).ifPresent(httpHeaders -> MDCUtil.putMDCTracking(loggingProperties, (HttpHeaders) httpHeaders));
    }
}
