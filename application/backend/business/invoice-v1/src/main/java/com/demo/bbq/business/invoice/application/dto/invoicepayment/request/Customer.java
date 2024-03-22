package com.demo.bbq.business.invoice.application.dto.invoicepayment.request;

import com.demo.bbq.business.invoice.application.constant.InvoiceRegex;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  @Pattern(regexp = InvoiceRegex.DOCUMENT_TYPE, message = "Invalid document type")
  private String documentType;

  private String documentNumber;
}
