package com.demo.poc.entrypoint.table.placement.dto.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDto implements Serializable{

  private String productCode;

  private Integer quantity;
}
