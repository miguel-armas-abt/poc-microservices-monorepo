package com.demo.bbq.entrypoint.inquiry.rest;

import java.util.function.Consumer;
import com.demo.bbq.entrypoint.processor.service.PaymentService;
import com.demo.bbq.entrypoint.processor.event.order.message.PaymentOrderMessage;
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

  @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Observable<PaymentOrderMessage> findAll(HttpServletRequest servletRequest) {
    logRequest.accept(servletRequest);
    return paymentService.findAll();
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info("{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
