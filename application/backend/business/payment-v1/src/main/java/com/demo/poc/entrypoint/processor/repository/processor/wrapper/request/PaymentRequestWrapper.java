package com.demo.poc.entrypoint.processor.repository.processor.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestWrapper implements Serializable {

  private String clientCompany;
  private BigDecimal amount;
}
