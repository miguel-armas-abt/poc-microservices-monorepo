package com.demo.bbq.business.invoice.application.dto.invoicepayment.request;

import java.io.Serializable;
import java.util.List;

import com.demo.bbq.business.invoice.application.dto.proformainvoice.request.ProductRequestDTO;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO implements Serializable {

  private List<ProductRequestDTO> productList;

  private CustomerDTO customer;

  private PaymentDTO payment;
}
