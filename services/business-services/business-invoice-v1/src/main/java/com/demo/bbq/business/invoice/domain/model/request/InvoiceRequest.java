package com.demo.bbq.business.invoice.domain.model.request;

import com.demo.bbq.business.invoice.domain.constant.InvoiceRegex;
import com.demo.bbq.business.invoice.infrastructure.documentation.data.InvoiceExample;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest implements Serializable {

  @Schema(example = InvoiceExample.TABLE_NUMBER)
  private Integer tableNumber;

  private Customer customer;

  @Pattern(regexp = InvoiceRegex.PAYMENT_METHOD, message = "Invalid payment method")
  private String paymentMethod;

  private Integer paymentInstallments;
}
