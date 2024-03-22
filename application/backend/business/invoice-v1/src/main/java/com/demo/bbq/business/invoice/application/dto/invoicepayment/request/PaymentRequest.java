package com.demo.bbq.business.invoice.application.dto.invoicepayment.request;

import java.io.Serializable;
import java.util.List;

import com.demo.bbq.business.invoice.application.dto.proformainvoice.request.ProductRequest;
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
