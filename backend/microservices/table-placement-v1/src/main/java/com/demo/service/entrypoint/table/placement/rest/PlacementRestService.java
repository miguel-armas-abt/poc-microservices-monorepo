package com.demo.service.entrypoint.table.placement.rest;

import static org.springframework.http.MediaType.APPLICATION_NDJSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PlacementRestService {

  private static final String BASE_URI = "/poc/business/table-placement/v1";
  private final static String TABLE_ORDERS_RESOURCE = "/table-orders";

  @Bean
  public RouterFunction<ServerResponse> buildTableOrdersRoutes(PlacementHandler placementHandler) {
    return nest(
        path(BASE_URI),
        route()
            .GET(TABLE_ORDERS_RESOURCE, accept(APPLICATION_NDJSON) , placementHandler::findByTableNumber)
            .DELETE(TABLE_ORDERS_RESOURCE, accept(APPLICATION_NDJSON), placementHandler::cleanTable)
            .PATCH(TABLE_ORDERS_RESOURCE, accept(APPLICATION_NDJSON), placementHandler::generateTableOrder)
            .build()
    );
  }
}