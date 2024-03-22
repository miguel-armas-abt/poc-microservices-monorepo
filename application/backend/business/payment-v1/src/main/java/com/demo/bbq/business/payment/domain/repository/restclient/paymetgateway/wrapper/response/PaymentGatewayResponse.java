package com.demo.bbq.business.payment.domain.repository.restclient.paymetgateway.wrapper.response;

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
