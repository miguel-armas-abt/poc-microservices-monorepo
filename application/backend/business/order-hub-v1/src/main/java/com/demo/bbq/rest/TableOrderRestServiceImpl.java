package com.demo.bbq.rest;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.demo.bbq.rest.handler.TablePlacementHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TableOrderRestServiceImpl {

  private static final String BASE_URI = "/bbq/bff/order-hub/v1/";

  @Bean("table-orders")
  public RouterFunction<ServerResponse> build(TablePlacementHandler tablePlacementHandler) {
    return nest(
        path(BASE_URI),
        route()
            .PATCH("table-orders", accept(APPLICATION_STREAM_JSON) , tablePlacementHandler::generateTableOrder)
            .GET("table-orders", accept(APPLICATION_STREAM_JSON), tablePlacementHandler::findByTableNumber)
            .build()
    );
  }
}