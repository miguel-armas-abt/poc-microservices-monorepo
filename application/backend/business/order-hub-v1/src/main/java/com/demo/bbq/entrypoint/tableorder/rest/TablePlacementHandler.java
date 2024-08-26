package com.demo.bbq.entrypoint.tableorder.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.toolkit.params.filler.QueryParamFiller.extractQueryParamsAsMap;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.tableorder.dto.MenuOrderRequestDTO;
import com.demo.bbq.entrypoint.tableorder.params.pojo.TableNumberParam;
import com.demo.bbq.entrypoint.tableorder.service.TablePlacementService;
import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
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
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    paramValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return serverRequest.bodyToFlux(MenuOrderRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .collectList()
        .flatMap(requestedMenuOrders -> tablePlacementService.generateTableOrder(headers, requestedMenuOrders, tableNumberParam.getTableNumber()))
        .then(ServerResponseFactory.buildEmpty(serverRequest.headers()));
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    paramValidator.validate(headers, DefaultHeaders.class);
    TableNumberParam tableNumberParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), TableNumberParam.class);

    return tablePlacementService.findByTableNumber(headers, tableNumberParam.getTableNumber())
        .flatMap(response -> ServerResponseFactory.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
