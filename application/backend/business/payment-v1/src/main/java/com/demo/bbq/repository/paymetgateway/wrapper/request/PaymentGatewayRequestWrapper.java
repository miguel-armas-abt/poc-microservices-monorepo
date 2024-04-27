package com.demo.bbq.repository.paymetgateway.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayRequestWrapper implements Serializable {

  private String clientCompany;
  private BigDecimal amount;
}
