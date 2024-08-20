package com.demo.bbq.entrypoint.inquiry.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceResponseDTO implements Serializable {

  private Long id;
  private String billingDate;
  private BigDecimal igv;
  private String paymentMethod;
  private String paymentStatus;
}