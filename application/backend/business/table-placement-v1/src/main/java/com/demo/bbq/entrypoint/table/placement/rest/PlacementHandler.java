package com.demo.bbq.entrypoint.table.placement.rest;

import com.demo.bbq.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.bbq.entrypoint.table.placement.service.PlacementService;
import com.demo.bbq.commons.toolkit.router.ServerResponseBuilderUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlacementHandler {

  private static final String PARAM_TABLE_NUMBER = "tableNumber";

  private final PlacementService placementService;

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return placementService.findByTableNumber(tableNumber.get())
        .flatMap(response -> ServerResponseBuilderUtil
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  public Mono<ServerResponse> cleanTable(ServerRequest serverRequest) {
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return ServerResponseBuilderUtil
        .buildEmpty(ServerResponse.ok(), serverRequest.headers(), placementService.cleanTable(tableNumber.get()));
  }

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    Flux<MenuOrderDTO> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderDTO.class);
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return ServerResponseBuilderUtil
        .buildEmpty(ServerResponse.ok(), serverRequest.headers(), placementService.generateTableOrder(requestedMenuOrders, tableNumber.get()));
  }
}
