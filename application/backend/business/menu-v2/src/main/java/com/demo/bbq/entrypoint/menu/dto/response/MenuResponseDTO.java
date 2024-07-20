package com.demo.bbq.entrypoint.menu.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDTO implements Serializable {

  private String productCode;

  private BigDecimal unitPrice;

  private String description;

  private String category;
}