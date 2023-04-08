package com.demo.bbq.business.invoice.domain.model.request;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest implements Serializable {

  private Integer tableNumber;

}