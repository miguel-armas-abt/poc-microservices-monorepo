package com.demo.bbq.entrypoint.calculator.rest;

import com.demo.bbq.commons.core.restserver.ServerResponseBuilder;
import com.demo.bbq.commons.core.validations.body.BodyValidator;
import com.demo.bbq.commons.core.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.core.validations.headers.HeaderValidator;
import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.service.CalculatorService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class CalculatorHandler {

  private final CalculatorService calculatorService;
  private final BodyValidator bodyValidator;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    return calculatorService.calculateInvoice(headers, serverRequest.bodyToFlux(ProductRequestDTO.class).doOnNext(bodyValidator::validate))
        .flatMap(response -> ServerResponseBuilder
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
