package com.demo.bbq.business.invoice.domain.model.request;

import java.io.Serializable;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {

  private List<ProductRequest> productList;

  private Customer customer;

  private Payment payment;
}
