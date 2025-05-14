package com.demo.poc.entrypoint.payment.dto;

import com.demo.poc.entrypoint.payment.constant.InvoiceRegex;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto implements Serializable {

  @Pattern(regexp = InvoiceRegex.DOCUMENT_TYPE, message = "Invalid document type")
  @NotNull
  private String documentType;

  @NotNull
  private String documentNumber;
}
