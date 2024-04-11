package com.demo.bbq.business.payment.domain.repository.paymetgateway;

import com.demo.bbq.business.payment.domain.repository.paymetgateway.wrapper.request.PaymentGatewayRequestWrapper;
import com.demo.bbq.business.payment.domain.repository.paymetgateway.wrapper.response.PaymentGatewayResponseWrapper;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRepository {

  public PaymentGatewayResponseWrapper process(PaymentGatewayRequestWrapper request) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    future.join();
    return PaymentGatewayResponseWrapper.builder()
        .isSuccessfulTransaction(Boolean.TRUE)
        .build();
  }
}
