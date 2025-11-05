package com.demo.service.entrypoint.invoice.repository.wrapper.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWrapper implements Serializable {
  private String method;
}
