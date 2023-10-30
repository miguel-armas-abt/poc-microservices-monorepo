package com.demo.bbq.business.invoice.domain.model.request;

import com.demo.bbq.business.invoice.infrastructure.documentation.data.InvoiceExample;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest implements Serializable {

  @Schema(example = InvoiceExample.TABLE_NUMBER)
  private Integer tableNumber;

}
