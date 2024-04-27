package com.demo.bbq.repository.invoice.wrapper.request;

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
