package com.demo.poc.entrypoint.menu.rest;

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
public class MenuRestServiceImpl {

  private static final String BASE_URI = "/poc/bff/order-hub/v1/";

  @Bean(name = "menu-options")
  public RouterFunction<ServerResponse> build(MenuHandler menuHandler) {
    return nest(
        path(BASE_URI),
        route()
            .GET("menu-options", accept(APPLICATION_NDJSON) , menuHandler::findMenuByCategory)
            .build()
    );
  }
}