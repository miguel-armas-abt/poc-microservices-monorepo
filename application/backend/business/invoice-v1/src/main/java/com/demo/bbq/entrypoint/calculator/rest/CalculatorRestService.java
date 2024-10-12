package com.demo.bbq.entrypoint.calculator.rest;

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
public class CalculatorRestService {

  private static final String BASE_URI = "/bbq/business/invoice/v1";

  @Bean
  public RouterFunction<ServerResponse> calculatorRoutes(CalculatorHandler calculatorHandler) {
    return nest(
        path(BASE_URI),
        route()
            .POST("/calculate", accept(APPLICATION_NDJSON) , calculatorHandler::calculateInvoice)
            .build()
    );
  }
}
