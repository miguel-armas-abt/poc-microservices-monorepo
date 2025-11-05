package com.demo.service.entrypoint.invoice.repository.wrapper.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestWrapper implements Serializable {

  private List<ProductRequestWrapper> productList;

  private CustomerWrapper customer;

  private PaymentWrapper payment;
}
