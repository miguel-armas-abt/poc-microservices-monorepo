package com.demo.service.entrypoint.payment.rest;

import com.demo.service.entrypoint.payment.repository.processor.PaymentProcessorRepository;
import com.demo.service.entrypoint.payment.repository.processor.wrapper.request.PaymentRequestWrapper;
import com.demo.service.entrypoint.payment.repository.processor.wrapper.response.PaymentResponseWrapper;
import io.reactivex.rxjava3.core.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(produces = "application/x-ndjson", value = "/poc/business/payment/v1")
public class PaymentRestService {

  private final PaymentProcessorRepository repository;

  @PostMapping("/execute")
  public Observable<PaymentResponseWrapper> execute() {
    return repository.execute(PaymentRequestWrapper.builder().build());
  }

}
