package com.demo.bbq.business.invoice.domain.model.response;

import com.demo.bbq.business.invoice.infrastructure.documentation.data.InvoiceExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProformaInvoice implements Serializable {

  private List<MenuOrder> menuOrderList;

  @Schema(example = InvoiceExample.SUBTOTAL)
  private BigDecimal subtotal;

  @Schema(example = InvoiceExample.IGV)
  private BigDecimal igv;

  @Schema(example = InvoiceExample.TOTAL)
  private BigDecimal total;
}
