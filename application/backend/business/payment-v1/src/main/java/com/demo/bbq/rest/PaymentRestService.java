package com.demo.bbq.rest;

import java.util.function.Consumer;
import com.demo.bbq.application.service.PaymentService;
import com.demo.bbq.application.events.consumer.message.PaymentMessage;
import io.reactivex.rxjava3.core.Observable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/business/v1/payments")
public class PaymentRestService {

  private final PaymentService paymentService;

  @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Observable<PaymentMessage> findAll(HttpServletRequest servletRequest) {
    logRequest.accept(servletRequest);
    return paymentService.findAll();
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info("{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
