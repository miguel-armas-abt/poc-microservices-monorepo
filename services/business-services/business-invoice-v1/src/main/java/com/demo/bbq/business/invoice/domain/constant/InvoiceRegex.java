package com.demo.bbq.business.invoice.domain.constant;

public class InvoiceRegex {
  private InvoiceRegex() {}

  public static final String DOCUMENT_TYPE = "^(DNI|PASSPORT)$";

  public static final String PAYMENT_METHOD = "^(DEBITO|CREDITO)$";
}
