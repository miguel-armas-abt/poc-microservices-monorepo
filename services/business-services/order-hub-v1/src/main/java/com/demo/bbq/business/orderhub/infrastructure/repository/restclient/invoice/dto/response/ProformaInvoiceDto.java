package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProformaInvoiceDto implements Serializable {

  private List<Product> productList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
