package com.demo.service.entrypoint.menu.repository.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionSaveRequestWrapper implements Serializable {

  private String productCode;
  private String description;
  private String category;
  private BigDecimal unitPrice;
}
