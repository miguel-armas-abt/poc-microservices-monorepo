package com.demo.bbq.entrypoint.table.placement.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.toolkit.params.filler.QueryParamFiller.extractQueryParamsAsMap;

import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.bbq.entrypoint.table.placement.params.pojo.TableNumberParam;
import com.demo.bbq.entrypoint.table.placement.service.PlacementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    paramValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return placementService.findByTableNumber(tableNumberParam.getTableNumber())
        .flatMap(response -> ServerResponseFactory
            .buildMono(
                ServerResponse.ok(),
                serverRequest.headers(),
                response));
  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    paramValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return placementService.cleanTable(tableNumberParam.getTableNumber())
            .then(ServerResponseFactory.buildEmpty(serverRequest.headers()));
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    paramValidator.validate(extractHeadersAsMap(serverRequest), DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    Flux<MenuOrderDTO> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderDTO.class).doOnNext(bodyValidator::validate);

    return placementService.generateTableOrder(requestedMenuOrders, tableNumberParam.getTableNumber())
        .then(ServerResponseFactory.buildEmpty(serverRequest.headers()));
  }
}