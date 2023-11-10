package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  private String documentType;

  private String documentNumber;
}
