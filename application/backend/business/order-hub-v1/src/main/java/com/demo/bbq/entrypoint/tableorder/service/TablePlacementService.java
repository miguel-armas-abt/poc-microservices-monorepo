package com.demo.bbq.entrypoint.tableorder.service;

import com.demo.bbq.entrypoint.tableorder.dto.MenuOrderRequestDTO;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface TablePlacementService {

  Mono<Void> generateTableOrder(ServerRequest serverRequest, List<MenuOrderRequestDTO> requestedMenuOrderList, Integer tableNumber);

  Mono<TableOrderResponseWrapper> findByTableNumber(ServerRequest serverRequest, Integer tableNumber);
}
