package com.demo.bbq.repository.invoice.wrapper.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProformaInvoiceResponseWrapper implements Serializable {

  private List<ProductResponseWrapper> productResponseWrapperList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
