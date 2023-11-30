package com.demo.bbq.business.orderhub.domain.model.invoicepayment;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  private String documentType;
  private String documentNumber;
}
