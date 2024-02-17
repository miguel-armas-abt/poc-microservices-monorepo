package com.demo.bbq.business.tableplacement.infrastructure.rest;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.demo.bbq.business.tableplacement.infrastructure.rest.handler.TableOrderHandler;
import com.demo.bbq.business.tableplacement.infrastructure.rest.handler.TableRegistrationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TablePlacementRestService {

  private static final String BASE_URI = "/bbq/business/table-placement/v1";
  private final static String TABLE_ORDERS_RESOURCE = "/table-orders";
  private final static String TABLES_RESOURCE = "/tables";

  @Bean
  public RouterFunction<ServerResponse> buildTableOrdersRoutes(TableOrderHandler tableOrderHandler) {
    return nest(
        path(BASE_URI),
        route()
            .GET(TABLE_ORDERS_RESOURCE, accept(APPLICATION_STREAM_JSON) , tableOrderHandler::findByTableNumber)
            .DELETE(TABLE_ORDERS_RESOURCE, accept(APPLICATION_STREAM_JSON), tableOrderHandler::cleanTable)
            .PATCH(TABLE_ORDERS_RESOURCE, accept(APPLICATION_STREAM_JSON), tableOrderHandler::generateTableOrder)
            .build()
    );
  }

  @Bean
  public RouterFunction<ServerResponse> buildTablesRoutes(TableRegistrationHandler tableOrderHandler) {
    return nest(
        path(BASE_URI),
        route()
            .POST(TABLES_RESOURCE, accept(APPLICATION_STREAM_JSON) , tableOrderHandler::createTable)
            .build()
    );
  }
}