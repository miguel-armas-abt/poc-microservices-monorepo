package com.demo.bbq.application.dto.tableorder.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderRequestDTO implements Serializable{

  private String productCode;

  private Integer quantity;
}
