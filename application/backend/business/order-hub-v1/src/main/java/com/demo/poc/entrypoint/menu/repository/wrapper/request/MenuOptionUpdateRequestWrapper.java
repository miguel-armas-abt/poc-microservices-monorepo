package com.demo.poc.entrypoint.menu.repository.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionUpdateRequestWrapper implements Serializable {

  private String description;
  private String category;
  private BigDecimal unitPrice;
}
