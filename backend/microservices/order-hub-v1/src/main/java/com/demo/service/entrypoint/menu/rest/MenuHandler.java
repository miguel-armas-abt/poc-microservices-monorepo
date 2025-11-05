package com.demo.service.entrypoint.menu.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.menu.params.CategoryParam;
import com.demo.service.entrypoint.menu.repository.MenuRepository;
import com.demo.service.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
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

  private final MenuRepository menuRepository;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    Flux<MenuOptionResponseWrapper> response = paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, CategoryParam.class))
        .flatMapMany(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          CategoryParam categoryParam = tuple.getT2().getKey();
          return menuRepository.findByCategory(headers, categoryParam.getCategory());
        });

    return ServerResponse.ok()
        .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
        .contentType(MediaType.APPLICATION_NDJSON)
        .body(BodyInserters.fromPublisher(response, MenuOptionResponseWrapper.class));
  }
}
