package com.demo.service.entrypoint.tableorder.rest;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.service.entrypoint.tableorder.params.TableNumberParam;
import com.demo.service.entrypoint.tableorder.service.TablePlacementService;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TablePlacementHandler {

  private final TablePlacementService tablePlacementService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, TableNumberParam.class))
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          TableNumberParam tableNumberParam = tuple.getT2().getKey();

          return serverRequest.bodyToFlux(MenuOrderRequestDto.class)
              .flatMap(bodyValidator::validateAndGet)
              .collectList()
              .flatMap(menuOrders -> tablePlacementService.generateTableOrder(headers, menuOrders, tableNumberParam.getTableNumber()));
        })
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paramValidator.validateQueryParamsAndGet(serverRequest, TableNumberParam.class))
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          TableNumberParam tableNumberParam = tuple.getT2().getKey();

          return tablePlacementService.findByTableNumber(headers, tableNumberParam.getTableNumber());
        })
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
