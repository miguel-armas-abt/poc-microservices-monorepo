package com.demo.bbq.business.invoice.application.dto.proformainvoice.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest implements Serializable {

  private String productCode;
  private String description;
  private Integer quantity;

}
