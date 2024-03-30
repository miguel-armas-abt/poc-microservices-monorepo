package com.demo.bbq.business.orderhub.domain.repository.tableorder.wrapper;

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
