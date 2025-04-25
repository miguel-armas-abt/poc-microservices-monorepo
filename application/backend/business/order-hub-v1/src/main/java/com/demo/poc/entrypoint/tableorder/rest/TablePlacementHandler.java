package com.demo.poc.entrypoint.tableorder.rest;

import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.entrypoint.tableorder.dto.params.TableNumberParam;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.poc.entrypoint.tableorder.service.TablePlacementService;
import com.demo.poc.commons.core.restserver.RestServerUtils;

import java.util.Map;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.poc.commons.core.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

@Component
@RequiredArgsConstructor
public class TablePlacementHandler {

  private final TablePlacementService tablePlacementService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);

    Mono<TableNumberParam> tableNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(tableNumberParamMono)
        .flatMap(tuple -> serverRequest.bodyToFlux(MenuOrderRequestDto.class)
            .flatMap(bodyValidator::validateAndGet)
            .collectList()
            .flatMap(menuOrders -> tablePlacementService.generateTableOrder(headers, menuOrders, tuple.getT2().getTableNumber())))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);

    Mono<TableNumberParam> tableNumberParamMono = paramValidator.validateAndGet(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(tableNumberParamMono)
        .flatMap(tuple -> tablePlacementService.findByTableNumber(headers, tuple.getT2().getTableNumber()))
        .flatMap(response -> ServerResponse.ok()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(response)));
  }
}
