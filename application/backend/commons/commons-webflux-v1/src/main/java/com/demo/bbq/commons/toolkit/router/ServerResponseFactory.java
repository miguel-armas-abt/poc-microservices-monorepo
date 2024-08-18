package com.demo.bbq.commons.toolkit.router;

import static com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType.TRACE_ID;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerResponseFactory {

  public static <T> Mono<ServerResponse> buildMono(ServerResponse.BodyBuilder bodyBuilder,
                                                   ServerRequest.Headers requestHeaders,
                                                   T response) {
    return bodyBuilder
        .headers(httpHeaders -> buildHeaders(requestHeaders).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromValue(response));
  }

  public static Mono<ServerResponse> buildEmpty(ServerResponse.BodyBuilder bodyBuilder,
                                                ServerRequest.Headers requestHeaders,
                                                Mono<Void> voidResponse) {
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