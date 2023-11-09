package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request;

import lombok.*;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {

  private List<ProductRequestDto> productList;

  private Customer customer;

  private String paymentMethod;
}
