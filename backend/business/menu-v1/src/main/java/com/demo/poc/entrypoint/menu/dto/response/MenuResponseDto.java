package com.demo.poc.entrypoint.menu.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto implements Serializable {

  private String productCode;

  private BigDecimal unitPrice;

  private String description;

  private String category;
}