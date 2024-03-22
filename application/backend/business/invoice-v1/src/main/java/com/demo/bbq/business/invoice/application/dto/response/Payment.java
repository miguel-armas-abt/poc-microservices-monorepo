package com.demo.bbq.business.invoice.application.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

  BigDecimal totalAmount;
  String paymentMethod;
  Long invoiceId;

}
