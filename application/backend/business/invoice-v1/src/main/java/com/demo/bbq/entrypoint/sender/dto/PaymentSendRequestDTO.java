package com.demo.bbq.entrypoint.sender.dto;

import com.demo.bbq.entrypoint.calculator.dto.request.ProductRequestDTO;
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
