package com.demo.bbq.entrypoint.sender.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HttpHeadersFiller.extractHeadersAsMap;

import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.sender.dto.PaymentSendRequestDTO;
import com.demo.bbq.entrypoint.sender.service.PaymentSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentSenderHandler {

  private final PaymentSenderService paymentSenderService;
  private final BodyValidator bodyValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> sendToPay(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    paramValidator.validate(headers, DefaultHeaders.class);

    return serverRequest.bodyToMono(PaymentSendRequestDTO.class)
        .doOnNext(bodyValidator::validate)
        .flatMap(request -> paymentSenderService.sendToPay(headers, request))
        .then(ServerResponseFactory.buildEmpty(serverRequest.headers()));
  }
}
