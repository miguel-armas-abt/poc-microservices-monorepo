package com.demo.bbq.business.tableplacement.application.dto.tableplacement.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderResponse implements Serializable {

  private String productCode;

  private Integer quantity;

}
