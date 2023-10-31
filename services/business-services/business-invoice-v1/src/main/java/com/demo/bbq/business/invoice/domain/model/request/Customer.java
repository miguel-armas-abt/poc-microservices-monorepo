package com.demo.bbq.business.invoice.domain.model.request;

import com.demo.bbq.business.invoice.domain.constant.InvoiceRegex;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {

  private String name;

  @Pattern(regexp = InvoiceRegex.DOCUMENT_TYPE, message = "Invalid document type")
  private String documentType;

  private String documentNumber;
}
