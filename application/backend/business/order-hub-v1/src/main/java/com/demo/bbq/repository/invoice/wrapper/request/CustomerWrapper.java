package com.demo.bbq.repository.invoice.wrapper.request;

import lombok.*;
import java.io.Serializable;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerWrapper implements Serializable {

  private String documentType;

  private String documentNumber;
}
