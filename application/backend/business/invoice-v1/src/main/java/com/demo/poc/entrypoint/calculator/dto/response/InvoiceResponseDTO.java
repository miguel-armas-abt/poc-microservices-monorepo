package com.demo.poc.entrypoint.calculator.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponseDTO implements Serializable {

  private List<ProductDTO> productList;
  private BigDecimal subtotal;
  private BigDecimal igv;
  private BigDecimal total;

}
