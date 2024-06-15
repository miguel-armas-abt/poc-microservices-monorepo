package com.demo.bbq.application.dto.sender.request;

import com.demo.bbq.application.dto.calculator.request.ProductRequestDTO;
import java.io.Serializable;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestDTO implements Serializable {

  private List<ProductRequestDTO> productList;

  private CustomerDTO customer;

  private PaymentDTO payment;
}
