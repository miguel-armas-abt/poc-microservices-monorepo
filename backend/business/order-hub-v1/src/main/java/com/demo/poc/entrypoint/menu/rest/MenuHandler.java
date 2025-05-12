package com.demo.poc.entrypoint.menu.rest;

import com.demo.poc.commons.core.restserver.utils.RestServerUtils;
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

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositorySelector menuRepository;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    Map<String, String> headers = RestServerUtils.extractHeadersAsMap(serverRequest);

    Mono<CategoryParam> categoryParamMono = paramValidator.validateAndGet(RestServerUtils.extractQueryParamsAsMap(serverRequest), CategoryParam.class);

    Flux<MenuOptionResponseWrapper> response = paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(categoryParamMono)
        .flatMapMany(tuple -> menuRepository.selectStrategy().findByCategory(headers, tuple.getT2().getCategory()));

    return ServerResponse.ok()
        .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(response, MenuOptionResponseWrapper.class));
  }
}
