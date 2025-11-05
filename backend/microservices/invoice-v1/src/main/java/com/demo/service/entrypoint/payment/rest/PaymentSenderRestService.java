package com.demo.service.entrypoint.payment.rest;

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
public class PaymentSenderRestService {

  private static final String BASE_URI = "/poc/business/invoice/v1";

  @Bean
  public RouterFunction<ServerResponse> paymentSenderRoutes(PaymentSenderHandler paymentSenderHandler) {
    return nest(
        path(BASE_URI),
        route()
            .POST("/send-to-pay", accept(APPLICATION_NDJSON), paymentSenderHandler::sendToPay)
            .build()
    );
  }
}
