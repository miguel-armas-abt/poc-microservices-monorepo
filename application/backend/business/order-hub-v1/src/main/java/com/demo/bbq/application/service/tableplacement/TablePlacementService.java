package com.demo.bbq.application.service.tableplacement;

import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderResponseWrapper;
import java.util.List;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public interface TablePlacementService {

  Mono<Void> generateTableOrder(ServerRequest serverRequest, List<MenuOrderRequestDTO> requestedMenuOrderList, Integer tableNumber);

  Mono<TableOrderResponseWrapper> findByTableNumber(ServerRequest serverRequest, Integer tableNumber);
}
