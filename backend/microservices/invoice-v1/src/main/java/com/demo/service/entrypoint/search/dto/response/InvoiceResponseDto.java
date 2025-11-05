package com.demo.service.entrypoint.search.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceResponseDto implements Serializable {

  private Long id;
  private String billingDate;
  private BigDecimal igv;
  private String paymentMethod;
  private String paymentStatus;
}