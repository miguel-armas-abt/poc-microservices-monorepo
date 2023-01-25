package com.demo.bbq.business.invoice.domain.model.response;

import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice implements Serializable {

  private List<MenuOrder> menuOrderList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}