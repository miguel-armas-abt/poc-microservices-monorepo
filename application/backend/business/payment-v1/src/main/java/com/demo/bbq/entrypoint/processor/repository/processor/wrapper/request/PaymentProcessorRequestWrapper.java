package com.demo.bbq.entrypoint.processor.repository.processor.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentProcessorRequestWrapper implements Serializable {

  private String clientCompany;
  private BigDecimal amount;
}
