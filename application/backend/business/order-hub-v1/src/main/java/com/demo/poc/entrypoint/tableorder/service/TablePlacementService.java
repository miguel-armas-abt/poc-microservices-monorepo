package com.demo.poc.entrypoint.tableorder.service;

import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface TablePlacementService {

  Mono<Void> generateTableOrder(Map<String, String> headers, List<MenuOrderRequestDto> requestedMenuOrderList, Integer tableNumber);

  Mono<TableOrderResponseWrapper> findByTableNumber(Map<String, String> headers, Integer tableNumber);
}
