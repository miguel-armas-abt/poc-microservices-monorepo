package com.demo.poc.entrypoint.sender.rest;

import com.demo.poc.commons.core.restserver.ServerResponseBuilder;
import com.demo.poc.commons.core.validations.body.BodyValidator;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import com.demo.poc.commons.core.validations.headers.HeaderValidator;
import com.demo.poc.entrypoint.sender.dto.PaymentSendRequestDTO;
import com.demo.poc.entrypoint.sender.service.PaymentSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Map;

import static com.demo.poc.commons.core.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;

@Component
@RequiredArgsConstructor
public class PaymentSenderHandler {

  private final PaymentSenderService paymentSenderService;
  private final BodyValidator bodyValidator;
  private final HeaderValidator headerValidator;

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);

    return serverRequest.bodyToMono(PaymentSendRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(request -> paymentSenderService.sendToPay(headers, request))
        .then(ServerResponseBuilder.buildEmpty(serverRequest.headers()));
  }
}
