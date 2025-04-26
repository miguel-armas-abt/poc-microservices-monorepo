package com.demo.poc.commons.core.interceptor.restclient.request;

import java.nio.charset.StandardCharsets;

import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.logging.dto.RestRequestLog;
import com.demo.poc.commons.core.logging.enums.LoggingType;
import com.demo.poc.commons.core.tracing.enums.TraceParam;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.client.ClientRequest;

public class RestClientRequestDecorator extends ClientHttpRequestDecorator {

  private final ThreadContextInjector contextInjector;
  private final ClientRequest originalRequest;
  private final boolean isLoggerPresent;

  public RestClientRequestDecorator(ClientHttpRequest delegate,
                                    ThreadContextInjector contextInjector,
                                    ClientRequest originalRequest,
                                    boolean isLoggerPresent) {
    super(delegate);
    this.contextInjector = contextInjector;
    this.originalRequest = originalRequest;
    this.isLoggerPresent = isLoggerPresent;
  }

  @Override
  public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
    return DataBufferUtils
        .join(Flux.from(body))
        .flatMap(buffer -> {
          try {
            String payload = buffer.toString(StandardCharsets.UTF_8);
            generateTrace(payload);
            DataBuffer copy = buffer.factory().wrap(buffer.asByteBuffer());
            return super.writeWith(Mono.just(copy));
          } finally {
            DataBufferUtils.release(buffer);
          }
        });
  }

  @Override
  public Mono<Void> setComplete() {
    generateTrace(StringUtils.EMPTY);
    return super.setComplete();
  }

  private void generateTrace(String payload) {
    if (isLoggerPresent) {
      Mono.fromRunnable(() -> {
            RestRequestLog log = RestRequestLog.builder()
                .method(originalRequest.method().name())
                .uri(originalRequest.url().toString())
                .requestHeaders(originalRequest.headers().toSingleValueMap())
                .requestBody(payload)
                .traceParent(originalRequest.headers().getFirst(TraceParam.TRACE_PARENT.getKey()))
                .build();
            contextInjector.populateFromRestRequest(LoggingType.REST_CLIENT_REQ, log);
          })
          .subscribeOn(Schedulers.boundedElastic())
          .subscribe();
    }
  }

}
