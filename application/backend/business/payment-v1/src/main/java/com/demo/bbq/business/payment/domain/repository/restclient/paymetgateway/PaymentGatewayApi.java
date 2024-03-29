package com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway;

import com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.wrapper.request.PaymentGatewayRequest;
import com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.wrapper.response.PaymentGatewayResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentGatewayApi {

  public PaymentGatewayResponse process(PaymentGatewayRequest request) {
    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
      try {
        TimeUnit.SECONDS.sleep(5);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    future.join();
    return PaymentGatewayResponse.builder()
        .isSuccessfulTransaction(Boolean.TRUE)
        .build();
  }
}
