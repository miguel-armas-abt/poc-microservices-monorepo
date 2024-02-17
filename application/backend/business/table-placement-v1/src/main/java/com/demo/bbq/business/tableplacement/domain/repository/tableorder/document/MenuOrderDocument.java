package com.demo.bbq.business.tableplacement.domain.repository.tableorder.document;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrderDocument implements Serializable {

  private String productCode;

  private Integer quantity;
}