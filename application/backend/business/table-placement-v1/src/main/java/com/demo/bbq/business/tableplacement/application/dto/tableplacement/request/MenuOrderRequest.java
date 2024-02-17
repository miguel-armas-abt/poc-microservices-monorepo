package com.demo.bbq.business.tableplacement.application.dto.tableplacement.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequest implements Serializable{

  private String productCode;

  private Integer quantity;
}
