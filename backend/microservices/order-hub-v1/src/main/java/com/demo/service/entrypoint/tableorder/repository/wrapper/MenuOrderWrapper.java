package com.demo.service.entrypoint.tableorder.repository.wrapper;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderWrapper implements Serializable {

  private String productCode;

  private Integer quantity;

}
