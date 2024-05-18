package com.demo.bbq.utils.interceptor.logging;

import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_REQ_METHOD;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.TRACKING_INFO;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_REQ_URI;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_RES_HEADERS;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_RES_BODY;
import static com.demo.bbq.utils.interceptor.logging.constants.LoggingConstant.MDC_RES_STATUS;

import com.demo.bbq.utils.interceptor.logging.mdc.MDCUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.restclient.webclient.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
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

    public static Context captureRequest(ClientRequest request) {
        var httpHeaders = request.headers();
        return Context.of(TRACKING_INFO, httpHeaders);
    }

    public static Mono<ClientResponse> decorateResponse(ConfigurationBaseProperties properties,
                                                        List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                                        ClientRequest clientRequest,
                                                        ClientResponse clientResponse,
                                                        Context context) {
        return clientResponse
            .bodyToMono(String.class)
            .defaultIfEmpty(StringUtils.EMPTY)
            .flatMap(responseBody -> {

            generateLog(properties, headerObfuscationStrategies, clientRequest, clientResponse, responseBody, context);
            var responseHeaders = clientResponse.headers().asHttpHeaders();

            return Mono.just(ClientResponse.create(clientResponse.statusCode())
                .headers(headers -> headers.addAll(responseHeaders))
                .body(responseBody)
                .build());
        });
    }

    private static void generateLog(ConfigurationBaseProperties properties,
                                    List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                    ClientRequest clientRequest,
                                    ClientResponse response,
                                    String responseBody,
                                    Context context) {
        try {
            context.getOrEmpty(TRACKING_INFO)
                .ifPresent(httpHeaders -> MDCUtil.generateTracking(properties.getApplicationName(), (HttpHeaders) httpHeaders));

            putStatusCode(response);
            ThreadContext.put(MDC_REQ_METHOD, clientRequest.method().toString());
            ThreadContext.put(MDC_REQ_URI, clientRequest.url().toString());
            ThreadContext.put(MDC_RES_HEADERS, HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, response.headers().asHttpHeaders()));
            ThreadContext.put(MDC_RES_BODY, BodyObfuscatorUtil.process(properties.getObfuscation(), responseBody));
            log.info("HTTP Successful Response");

        } catch (Exception ex) {
            log.error("HTTP Response Error, Exception class: " + ex.getClass(), ex);
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
}