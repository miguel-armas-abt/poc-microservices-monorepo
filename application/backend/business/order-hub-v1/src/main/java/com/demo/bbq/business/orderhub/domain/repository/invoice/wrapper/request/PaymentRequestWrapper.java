package com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestWrapper implements Serializable {

  private List<ProductRequestWrapper> productList;

  private CustomerWrapper customer;

  private PaymentWrapper payment;
}
