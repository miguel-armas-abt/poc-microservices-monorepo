package com.demo.bbq.business.orderhub.domain.model.invoicepayment;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {
  private String method;
}
