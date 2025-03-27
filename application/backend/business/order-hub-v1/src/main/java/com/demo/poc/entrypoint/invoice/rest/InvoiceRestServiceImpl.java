package com.demo.poc.entrypoint.invoice.rest;

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
public class InvoiceRestServiceImpl {

  private static final String BASE_URI = "/poc/bff/order-hub/v1/invoices/";

  @Bean("invoices")
  public RouterFunction<ServerResponse> build(InvoiceHandler invoiceHandler) {
    return nest(
        path(BASE_URI),
        route()
            .POST("calculate", accept(APPLICATION_NDJSON) , invoiceHandler::calculateInvoice)
            .POST("send-to-pay", accept(APPLICATION_NDJSON), invoiceHandler::sendToPay)
            .build()
    );
  }
}