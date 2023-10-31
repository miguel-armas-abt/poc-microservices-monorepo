package com.demo.bbq.business.payment.infrastructure.repository.restclient.dto;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayResponse implements Serializable {

  private Boolean isSuccessfulTransaction;
}
