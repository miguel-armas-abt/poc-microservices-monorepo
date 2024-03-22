package com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayRequest implements Serializable {

  private String clientCompany;
  private BigDecimal amount;
}
