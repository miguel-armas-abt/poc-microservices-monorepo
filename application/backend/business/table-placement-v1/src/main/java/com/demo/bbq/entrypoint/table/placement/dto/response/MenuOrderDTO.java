package com.demo.bbq.entrypoint.table.placement.dto.response;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDTO implements Serializable {

  private String productCode;

  private Integer quantity;

}
