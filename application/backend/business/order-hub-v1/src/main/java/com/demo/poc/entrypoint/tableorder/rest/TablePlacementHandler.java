package com.demo.poc.entrypoint.tableorder.rest;

import com.demo.poc.commons.core.validations.body.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.commons.core.validations.params.ParamValidator;
import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.entrypoint.tableorder.dto.params.TableNumberParam;
import com.demo.poc.entrypoint.tableorder.service.TablePlacementService;
import com.demo.poc.commons.core.restserver.ServerResponseBuilder;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return serverRequest.bodyToFlux(MenuOrderRequestDto.class)
        .doOnNext(bodyValidator::validate)
        .collectList()
        .flatMap(requestedMenuOrders -> tablePlacementService.generateTableOrder(headers, requestedMenuOrders, tableNumberParam.getTableNumber()))
        .then(ServerResponseBuilder.buildEmpty(serverRequest.headers()));
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return tablePlacementService.findByTableNumber(headers, tableNumberParam.getTableNumber())
        .flatMap(response -> ServerResponseBuilder.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
