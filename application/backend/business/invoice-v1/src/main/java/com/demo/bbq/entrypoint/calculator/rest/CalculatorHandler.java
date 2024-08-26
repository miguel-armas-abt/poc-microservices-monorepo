package com.demo.bbq.entrypoint.calculator.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;

import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
import com.demo.bbq.entrypoint.calculator.service.CalculatorService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CalculatorHandler {

  private final CalculatorService calculatorService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> calculateInvoice(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    paramValidator.validate(headers, DefaultHeaders.class);

    return calculatorService.calculateInvoice(headers, serverRequest.bodyToFlux(ProductRequestDTO.class).doOnNext(bodyValidator::validate))
        .flatMap(response -> ServerResponseFactory
            .buildMono(ServerResponse.ok(), serverRequest.headers(), response));
  }
}
