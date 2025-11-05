package com.demo.service.entrypoint.search.rest;

import java.util.function.Consumer;
import com.demo.service.entrypoint.payment.service.PaymentService;
import com.demo.service.entrypoint.payment.event.pay.message.PaymentInboundMessage;
import io.reactivex.rxjava3.core.Observable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/poc/business/v1/payments")
public class PaymentSearchRestService {

  private final PaymentService paymentService;

  @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Observable<PaymentInboundMessage> findAll(HttpServletRequest servletRequest) {
    logRequest.accept(servletRequest);
    return paymentService.findAll();
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info("{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
