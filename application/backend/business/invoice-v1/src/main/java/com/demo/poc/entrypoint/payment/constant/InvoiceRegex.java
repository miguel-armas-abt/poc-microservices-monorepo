package com.demo.poc.entrypoint.payment.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceRegex {

  public static final String DOCUMENT_TYPE = "^(DNI|PASSPORT)$";

}
