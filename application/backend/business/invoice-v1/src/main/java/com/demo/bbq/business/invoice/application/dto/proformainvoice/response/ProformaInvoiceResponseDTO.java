package com.demo.bbq.business.invoice.application.dto.proformainvoice.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProformaInvoiceResponseDTO implements Serializable {

  private List<ProductResponseDTO> productList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
