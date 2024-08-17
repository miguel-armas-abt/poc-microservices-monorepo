package com.demo.bbq.entrypoint.processor.repository.processor;

import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.request.PaymentProcessorRequestWrapper;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response.PaymentProcessorResponseWrapper;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRepository {

  public PaymentProcessorResponseWrapper process(PaymentProcessorRequestWrapper request) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    future.join();
    return PaymentProcessorResponseWrapper.builder()
        .isSuccessfulTransaction(Boolean.TRUE)
        .build();
  }
}
