package com.demo.poc.entrypoint.table.placement.rest;

import com.demo.poc.commons.core.restserver.ServerResponseBuilder;
import com.demo.poc.commons.core.validations.body.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.commons.core.validations.params.ParamValidator;
import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.poc.entrypoint.table.placement.dto.params.TableNumberParam;
import com.demo.poc.entrypoint.table.placement.service.PlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    headerValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return placementService.findByTableNumber(tableNumberParam.getTableNumber())
        .flatMap(response -> ServerResponseBuilder
            .buildMono(
                ServerResponse.ok(),
                serverRequest.headers(),
                response));
  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    headerValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return placementService.cleanTable(tableNumberParam.getTableNumber())
            .then(ServerResponseBuilder.buildEmpty(serverRequest.headers()));
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    headerValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    Flux<MenuOrderDTO> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderDTO.class).doOnNext(bodyValidator::validate);

    return placementService.generateTableOrder(requestedMenuOrders, tableNumberParam.getTableNumber())
        .then(ServerResponseBuilder.buildEmpty(serverRequest.headers()));
  }
}