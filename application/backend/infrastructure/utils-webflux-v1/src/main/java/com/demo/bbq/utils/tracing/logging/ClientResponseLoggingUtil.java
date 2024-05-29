package com.demo.bbq.utils.tracing.logging;

import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_ERROR_RESPONSE;
import static com.demo.bbq.utils.tracing.logging.constants.LoggingMessage.HTTP_RESPONSE;
import static com.demo.bbq.utils.tracing.logging.constants.ThreadContextConstant.TRACKING_INFO;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.tracing.logging.util.HeaderMapperUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
public class ClientResponseLoggingUtil {

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
                .ifPresent(httpHeaders -> ThreadContextInjectorUtil.populateFromHeaders(HeaderMapperUtil.recoverTraceHeaders((HttpHeaders) httpHeaders)));

            var method = clientRequest.method().toString();
            var uri = clientRequest.url().toString();
            var headers = HeaderObfuscatorUtil.process(properties.getObfuscation(), headerObfuscationStrategies, response.headers().asHttpHeaders().toSingleValueMap());
            var body = BodyObfuscatorUtil.process(properties.getObfuscation(), responseBody);

            ThreadContextInjectorUtil.populateFromClientResponse(method, uri, headers, body, getHttpCode(response));
            log.info(HTTP_RESPONSE);

        } catch (Exception ex) {
            log.error(HTTP_ERROR_RESPONSE + ex.getClass(), ex);
        }
        ThreadContext.clearAll();
    }

    private static String getHttpCode(ClientResponse response){
        try{
            return response.statusCode().toString();
        }catch (IllegalArgumentException ex){
            return String.valueOf(response.statusCode());
        }
    }
}