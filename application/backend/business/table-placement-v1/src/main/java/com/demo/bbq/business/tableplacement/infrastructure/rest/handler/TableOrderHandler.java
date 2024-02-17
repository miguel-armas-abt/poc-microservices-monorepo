package com.demo.bbq.business.tableplacement.infrastructure.rest.handler;

import com.demo.bbq.business.tableplacement.application.service.TablePlacementService;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.response.TablePlacementResponse;
import com.demo.bbq.business.tableplacement.infrastructure.rest.common.BuilderServerResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TableOrderHandler {

  private static final String PARAM_TABLE_NUMBER = "tableNumber";

  private final TablePlacementService tablePlacementService;
  private final BuilderServerResponse<TablePlacementResponse> buildTableOrderResponse;

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return tablePlacementService.findByTableNumber(tableNumber.get())
        .flatMap(buildTableOrderResponse::build);
  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return buildTableOrderResponse.buildVoid(tablePlacementService.cleanTable(tableNumber.get()));
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Flux<MenuOrderRequest> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderRequest.class);
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return buildTableOrderResponse.buildVoid(tablePlacementService.generateTableOrder(requestedMenuOrders, tableNumber.get()));
  }
}
