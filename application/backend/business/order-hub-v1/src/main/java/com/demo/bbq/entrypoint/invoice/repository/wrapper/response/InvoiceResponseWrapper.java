package com.demo.bbq.entrypoint.invoice.repository.wrapper.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseWrapper implements Serializable {

  private List<ProductResponseWrapper> productList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
