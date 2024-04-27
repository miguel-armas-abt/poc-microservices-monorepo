package com.demo.bbq.rest.handler;

import com.demo.bbq.application.dto.tableplacement.request.MenuOrderRequestDTO;
import com.demo.bbq.application.service.TablePlacementService;
import com.demo.bbq.application.dto.tableplacement.response.TablePlacementResponseDTO;
import com.demo.bbq.rest.common.BuilderServerResponse;
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
  private final BuilderServerResponse<TablePlacementResponseDTO> buildTableOrderResponse;

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
    Flux<MenuOrderRequestDTO> requestedMenuOrders = serverRequest.bodyToFlux(MenuOrderRequestDTO.class);
    Optional<Integer> tableNumber = serverRequest.queryParam(PARAM_TABLE_NUMBER).map(Integer::parseInt);

    return buildTableOrderResponse.buildVoid(tablePlacementService.generateTableOrder(requestedMenuOrders, tableNumber.get()));
  }
}
