package com.demo.poc.entrypoint.menu.rest;

import com.demo.poc.commons.core.restserver.RestServerUtils;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.menu.params.CategoryParam;
import com.demo.poc.entrypoint.menu.repository.MenuRepositorySelector;
import com.demo.poc.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.poc.commons.core.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositorySelector menuRepository;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);

    Mono<CategoryParam> categoryParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), CategoryParam.class);

    Flux<MenuOptionResponseWrapper> response = paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(categoryParamMono)
        .flatMapMany(tuple -> menuRepository.selectStrategy().findByCategory(headers, tuple.getT2().getCategory()));

    return stream(serverRequest.headers(), response);
  }

  private static Mono<ServerResponse> stream(ServerRequest.Headers requestHeaders,
                                             Flux<MenuOptionResponseWrapper> streamResponse) {
    return ServerResponse.ok()
        .headers(headers -> RestServerUtils.buildResponseHeaders(requestHeaders).accept(headers))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(streamResponse, MenuOptionResponseWrapper.class));
  }
}
