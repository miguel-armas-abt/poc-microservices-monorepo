package com.demo.service.entrypoint.table.placement.dto.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable {

  private String productCode;

  private Integer quantity;

}
