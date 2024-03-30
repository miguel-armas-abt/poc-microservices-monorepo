package com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestWrapper implements Serializable {

  private String description;
  private Integer quantity;
  private String productCode;

}
