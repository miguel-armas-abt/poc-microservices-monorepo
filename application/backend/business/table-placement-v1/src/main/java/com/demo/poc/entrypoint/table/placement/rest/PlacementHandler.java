package com.demo.poc.entrypoint.table.placement.rest;

import com.demo.poc.commons.core.restserver.RestServerUtils;
import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.poc.entrypoint.table.placement.dto.params.TableNumberParam;
import com.demo.poc.entrypoint.table.placement.service.PlacementService;
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
public class PlacementHandler {

  private final PlacementService placementService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Mono<TableNumberParam> tableNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return paramValidator.validateAndGet(extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .zipWith(tableNumberParamMono)
        .flatMap(tuple -> placementService.findByTableNumber(tuple.getT2().getTableNumber()))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));

  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    Mono<TableNumberParam> tableNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return paramValidator.validateAndGet(extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .zipWith(tableNumberParamMono)
        .flatMap(tuple -> placementService.cleanTable(tuple.getT2().getTableNumber()))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Mono<TableNumberParam> tableNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    Flux<MenuOrderDto> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class)
        .zipWith(tableNumberParamMono)
        .flatMap(tuple -> placementService.generateTableOrder(requestedMenuOrders, tuple.getT2().getTableNumber()))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }
}