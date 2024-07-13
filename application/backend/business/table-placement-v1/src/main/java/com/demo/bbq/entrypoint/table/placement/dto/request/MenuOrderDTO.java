package com.demo.bbq.entrypoint.table.placement.dto.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDTO implements Serializable{

  private String productCode;

  private Integer quantity;
}
