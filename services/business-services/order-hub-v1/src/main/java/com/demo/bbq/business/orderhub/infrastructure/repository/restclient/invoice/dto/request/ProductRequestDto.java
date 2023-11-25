package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto implements Serializable {

  private String description;
  private Integer quantity;
  private String productCode;

}
