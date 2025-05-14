package com.demo.poc.entrypoint.tableorder.dto.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequestDto implements Serializable{

  private String productCode;

  private Integer quantity;
}
