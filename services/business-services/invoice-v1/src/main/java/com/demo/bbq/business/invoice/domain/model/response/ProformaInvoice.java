package com.demo.bbq.business.invoice.domain.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProformaInvoice implements Serializable {

  private List<Product> productList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
