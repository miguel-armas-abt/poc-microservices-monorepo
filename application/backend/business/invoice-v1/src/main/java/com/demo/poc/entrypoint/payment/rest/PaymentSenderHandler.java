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

    Mono<PaymentSendRequestDto> paymentRequestMono = serverRequest.bodyToMono(PaymentSendRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateAndGet(headers, DefaultHeaders.class)
        .zipWith(paymentRequestMono)
        .flatMap(tuple -> paymentSenderService.sendToPay(headers, tuple.getT2()))
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }
}
