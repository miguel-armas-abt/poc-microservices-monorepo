package com.demo.bbq.rest.common;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class BuilderServerResponse<T> {
  public Mono<ServerResponse> build(T response) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .body(BodyInserters.fromValue(response));
  }

  public Mono<ServerResponse> buildVoid(Mono<Void> voidMono) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_STREAM_JSON)
        .body(voidMono, Void.class);
  }
}