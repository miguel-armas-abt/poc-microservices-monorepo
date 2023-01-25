package com.demo.bbq.business.payment.infrastructure.resource.rest;

import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import com.demo.bbq.business.payment.application.service.PaymentService;
import com.demo.bbq.business.payment.domain.model.response.Payment;
import com.demo.bbq.support.logstash.Markers;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/business/v1/payments")
public class PaymentApi {

  private final PaymentService paymentService;

  @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Observable<Payment> findAll(HttpServletRequest servletRequest) {
    logRequest.accept(servletRequest);
    return paymentService.findAll();
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
