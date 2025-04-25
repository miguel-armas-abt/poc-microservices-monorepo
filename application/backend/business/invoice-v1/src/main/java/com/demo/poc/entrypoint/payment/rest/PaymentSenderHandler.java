package com.demo.poc.entrypoint.payment.rest;

import java.util.Map;

import com.demo.poc.commons.core.restserver.RestServerUtils;
import com.demo.poc.commons.core.validations.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.ParamValidator;
import com.demo.poc.entrypoint.payment.dto.PaymentSendRequestDto;
import com.demo.poc.entrypoint.payment.service.PaymentSenderService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class PaymentSenderHandler {

  private final PaymentSenderService paymentSenderService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .flatMap(defaultHeaders -> serverRequest.bodyToMono(PaymentSendRequestDto.class)
            .doOnNext(bodyValidator::validate)
            .flatMap(request -> paymentSenderService.sendToPay(headers, request)))
        .then(build(serverRequest.headers()));
  }

  private static Mono<ServerResponse> build(ServerRequest.Headers requestHeaders) {
    return ServerResponse.noContent()
        .headers(headers -> RestServerUtils.buildResponseHeaders(requestHeaders).accept(headers))
        .build();
  }
}
