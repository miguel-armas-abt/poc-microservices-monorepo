package com.demo.bbq.utils.config.webclient.logging;

import static com.demo.bbq.utils.config.webclient.logging.constants.LoggingConstant.MDC_REQ_METHOD;
import static com.demo.bbq.utils.config.webclient.logging.constants.LoggingConstant.MDC_REQ_URI;
import static com.demo.bbq.utils.config.webclient.logging.constants.LoggingConstant.MDC_REQ_BODY;
import static com.demo.bbq.utils.config.webclient.logging.constants.LoggingConstant.MDC_REQ_HEADERS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.demo.bbq.utils.config.webclient.logging.mdc.MDCUtil;
import com.demo.bbq.utils.config.webclient.obfuscation.body.BodyObfuscatorUtil;
import com.demo.bbq.utils.config.webclient.obfuscation.header.HeaderObfuscatorUtil;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.config.webclient.properties.LoggingBaseProperties;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import reactor.core.publisher.Mono;

@Slf4j
public class ExchangeRequestFilterUtil {

    public static ClientRequest buildClientRequestDecorator(LoggingBaseProperties properties,
                                                            List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                                            ClientRequest request) {

        BodyInserter<?, ? super ClientHttpRequest> originalBodyInserter = request.body();
        return ClientRequest.from(request)
                .body((outputMessage, context) -> {
                    ClientHttpRequestDecorator loggingOutputMessage = new ClientHttpRequestDecorator(outputMessage) {

                        @Override
                        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                            return DataBufferUtils.join(body)
                                    .flatMap(buffer -> {
                                        var content = buffer.toString(StandardCharsets.UTF_8);
                                        createLogs(properties, headerObfuscationStrategies, request, content);
                                        DataBuffer copyBuffer = buffer.factory().wrap(buffer.asByteBuffer());
                                        return super.writeWith(Mono.just(copyBuffer))
                                                .doFinally(signalType -> DataBufferUtils.release(buffer));
                                    });
                        }

                        @Override
                        public Mono<Void> setComplete() {
                            createLogs(properties, headerObfuscationStrategies, request, EMPTY);
                            return super.setComplete();
                        }
                    };
                    return originalBodyInserter.insert(loggingOutputMessage, context);
                })
                .build();
    }

    private static void createLogs(LoggingBaseProperties loggingProperties,
                                   List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                   ClientRequest clientRequest,
                                   String requestBody) {
        try {
            var method = clientRequest.method().toString();
            var uri = clientRequest.url().toString();
            var httpHeaders = clientRequest.headers();

            MDCUtil.putMDCTracking(loggingProperties, httpHeaders);

            ThreadContext.put(MDC_REQ_METHOD, method);
            ThreadContext.put(MDC_REQ_URI, uri);
            ThreadContext.put(MDC_REQ_HEADERS, HeaderObfuscatorUtil.process(loggingProperties, headerObfuscationStrategies, httpHeaders));
            ThreadContext.put(MDC_REQ_BODY, BodyObfuscatorUtil.process(loggingProperties, requestBody));

            log.info("HTTP Request - Successful method execution");

        } catch (Exception ex) {
            log.error("Error logWebClientRequest, Exception class: " + ex.getClass(), ex);
        }
        ThreadContext.clearAll();
    }
}

