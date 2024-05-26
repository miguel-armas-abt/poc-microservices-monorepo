package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.application.service.tableplacement.TablePlacementService;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.toolkit.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TablePlacementHandler {

  private final TablePlacementService tablePlacementService;

  public Mono<ServerResponse> generateTableOrder(ServerRequest serverRequest) {
    return ServerResponseBuilderUtil
        .buildEmpty(
            ServerResponse.ok(),
            serverRequest.headers(),
            serverRequest.bodyToFlux(MenuOrderRequestDTO.class)
                .collectList()
                .flatMap(requestedMenuOrders -> tablePlacementService.generateTableOrder(serverRequest, requestedMenuOrders, getTableNumber(serverRequest)))
        );
  }

  public Mono<ServerResponse> findByTableNumber(ServerRequest serverRequest) {
    return tablePlacementService.findByTableNumber(serverRequest, getTableNumber(serverRequest))
        .flatMap(response -> ServerResponseBuilderUtil.buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }

  private int getTableNumber(ServerRequest serverRequest) {
    return serverRequest.queryParam("tableNumber")
        .map(Integer::parseInt)
        .orElseThrow(() -> new BusinessException("Invalid table number"));
  }
}
