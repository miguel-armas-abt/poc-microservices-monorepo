package com.demo.service.entrypoint.search.rest;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class InvoiceSearchRestService {

  private static final String BASE_URI = "/poc/business/invoice/v1";

  @Bean
  public RouterFunction<ServerResponse> invoiceInquiryRoutes(InvoiceSearchHandler invoiceSearchHandler) {
    return nest(
        path(BASE_URI),
        route()
            .GET("/invoices", accept(MediaType.APPLICATION_NDJSON), invoiceSearchHandler::findInvoicesByCustomer)
            .build()
    );
  }
}
