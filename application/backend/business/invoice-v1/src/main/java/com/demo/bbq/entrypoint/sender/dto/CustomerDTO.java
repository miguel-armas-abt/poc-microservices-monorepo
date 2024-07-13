package com.demo.bbq.entrypoint.sender.dto;

import com.demo.bbq.entrypoint.sender.constant.InvoiceRegex;
import java.io.Serializable;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO implements Serializable {

  @Pattern(regexp = InvoiceRegex.DOCUMENT_TYPE, message = "Invalid document type")
  private String documentType;

  private String documentNumber;
}
