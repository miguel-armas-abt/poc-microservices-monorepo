package com.demo.poc.commons.core.restserver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;

import static com.demo.poc.commons.core.tracing.enums.TraceParamType.TRACE_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerResponseBuilder {

  public static <T> Mono<ServerResponse> buildMono(ServerResponse.BodyBuilder bodyBuilder,
                                                   ServerRequest.Headers requestHeaders,
                                                   T response) {
    return bodyBuilder
        .headers(httpHeaders -> buildHeaders(requestHeaders).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromValue(response));
  }

  public static Mono<ServerResponse> buildEmpty(ServerRequest.Headers requestHeaders) {
    return ServerResponse.noContent()
        .headers(httpHeaders -> buildHeaders(requestHeaders).accept(httpHeaders))
        .build();
  }

  public static <T> Mono<ServerResponse> buildFlux(ServerResponse.BodyBuilder bodyBuilder,
                                                   ServerRequest.Headers requestHeaders,
                                                   Class<T> elementClass,
                                                   Flux<T> streamResponse) {
    return bodyBuilder
        .headers(httpHeaders -> buildHeaders(requestHeaders).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(streamResponse, elementClass));
  }

  private static Consumer<HttpHeaders> buildHeaders(ServerRequest.Headers requestHeaders) {
    return currentHeaders -> Optional
        .ofNullable(requestHeaders.firstHeader(TRACE_ID.getKey()))
        .ifPresentOrElse(traceId -> currentHeaders.set(TRACE_ID.getKey(), traceId)
            , () -> {});
  }

}