package com.demo.poc.entrypoint.search.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto implements Serializable {

  private Long id;
  private BigDecimal totalAmount;
  private String paymentMethod;
  private Long invoiceId;
}
