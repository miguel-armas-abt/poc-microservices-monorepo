package com.demo.poc.entrypoint.payment.dto;

import com.demo.poc.entrypoint.calculator.dto.request.ProductRequestDto;
import java.io.Serializable;
import java.util.List;
import jakarta.validation.Valid;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSendRequestDto implements Serializable {

  @Valid
  private List<ProductRequestDto> productList;

  @Valid
  private CustomerDto customer;

  @Valid
  private PaymentDto payment;
}
