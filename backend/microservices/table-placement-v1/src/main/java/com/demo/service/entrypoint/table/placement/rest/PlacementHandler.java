package com.demo.service.entrypoint.table.placement.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.service.entrypoint.table.placement.params.TableNumberParam;
import com.demo.service.entrypoint.table.placement.service.PlacementService;
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
public class PlacementHandler {

  private final PlacementService placementService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, TableNumberParam.class))
        .flatMap(tuple -> {
          TableNumberParam tableNumberParam = tuple.getT2().getKey();
          return placementService.findByTableNumber(tableNumberParam.getTableNumber());
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));

  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, TableNumberParam.class))
        .flatMap(tuple -> {
          TableNumberParam tableNumberParam = tuple.getT2().getKey();
          return placementService.cleanTable(tableNumberParam.getTableNumber());
        })
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Flux<MenuOrderDto> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, TableNumberParam.class))
        .flatMap(tuple -> {
          TableNumberParam tableNumberParam = tuple.getT2().getKey();
          return placementService.generateTableOrder(requestedMenuOrders, tableNumberParam.getTableNumber());
        })
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }
}