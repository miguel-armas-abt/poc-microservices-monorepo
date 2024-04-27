package com.demo.bbq.application.dto.tableplacement.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderResponseDTO implements Serializable {

  private String productCode;

  private Integer quantity;

}
