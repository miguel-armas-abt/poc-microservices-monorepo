package com.demo.bbq.repository.paymetgateway.wrapper.response;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentGatewayResponseWrapper implements Serializable {

  private Boolean isSuccessfulTransaction;
}
