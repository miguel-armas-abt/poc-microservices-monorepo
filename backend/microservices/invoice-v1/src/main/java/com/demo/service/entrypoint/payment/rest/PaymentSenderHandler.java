package com.demo.service.entrypoint.payment.rest;

import java.util.Map;

import com.demo.commons.restserver.utils.RestServerUtils;
import com.demo.commons.validations.BodyValidator;
import com.demo.commons.validations.ParamValidator;
import com.demo.commons.validations.headers.DefaultHeaders;
import com.demo.service.entrypoint.payment.dto.PaymentSendRequestDto;
import com.demo.service.entrypoint.payment.service.PaymentSenderService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PaymentSenderHandler {

  private final PaymentSenderService paymentSenderService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Mono<PaymentSendRequestDto> paymentRequestMono = serverRequest.bodyToMono(PaymentSendRequestDto.class)
        .flatMap(bodyValidator::validateAndGet);

    return paramValidator.validateHeadersAndGet(serverRequest, DefaultHeaders.class)
        .zipWith(paymentRequestMono)
        .flatMap(tuple -> {
          Map<String, String> headers = tuple.getT1().getValue();
          return paymentSenderService.sendToPay(headers, tuple.getT2());
        })
        .then(ServerResponse.noContent()
            .headers(httpHeaders -> RestServerUtils.buildResponseHeaders(serverRequest.headers()).accept(httpHeaders))
            .build());
  }
}
